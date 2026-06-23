package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.model.PlayerProperty;
import hr.algebra.head_soccer_2d_game.server.model.PlayerPropsTag;
import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import javafx.scene.paint.Color;
import lombok.Synchronized;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class XMLUtils {
    private static final String DOCTYPE = "DOCTYPE";
    private static final String DTD = "dtd/PlayerProperties.dtd";
    private static final String PLAYER_PROPS = "PlayerProperties";
    private static final String FILENAME = "xml/PlayerProperties.xml";

    @Synchronized
    public  static void saveNewPlayerProp(PlayerProperty playerProperty) {
        List<PlayerProperty> playerPropertyList;
        try {
            playerPropertyList = loadPlayerProps();
            Document document = createDocument(PLAYER_PROPS);

            playerPropertyList.add(playerProperty);
            for (PlayerProperty nextPlayerProperty : playerPropertyList) {
                appendPlayerProperty(nextPlayerProperty, document);
            }

            saveDocument(document, FILENAME);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            log.error("XML save error: {}", e.getMessage());
        }
    }

    private static void saveDocument(Document document, String filename) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, DTD);
        transformer.transform(new DOMSource(document), new StreamResult(new File(filename)));
    }

    private static void appendPlayerProperty(PlayerProperty playerProperty, Document document) {
        Element element = document.createElement(PlayerPropsTag.PLAYER_PROPERTY.getTagName());
        document.getDocumentElement().appendChild(element);
        element.appendChild(
                createElement(document, PlayerPropsTag.PLAYER_NAME.getTagName(),
                        playerProperty.getPlayerName().toString())
        );
        element.appendChild(
                createElement(document, PlayerPropsTag.COLOR.getTagName(),
                        playerProperty.getColor().toString())
        );
        element.appendChild(
                createElement(document, PlayerPropsTag.PLAYER_TYPE.getTagName(),
                        playerProperty.getPlayerType().toString())
        );
    }

    private static Node createElement(Document document, String tagName, String string) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(string);
        element.appendChild(text);
        return element;
    }

    private static Document createDocument(String element) throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        DocumentType docType = dom.createDocumentType(DOCTYPE, null, DTD);
        return dom.createDocument(null, element, docType);
    }

    @Synchronized
    public static List<PlayerProperty> loadPlayerProps() throws ParserConfigurationException,
            IOException, SAXException {
        return parse(FILENAME);
    }

    private static List<PlayerProperty> parse(String path) throws ParserConfigurationException,
            IOException, SAXException {

        if (!Files.exists(Path.of(path)) || new File(path).length() == 0) {
            return new ArrayList<>();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {

            @Override
            public void warning(SAXParseException exception) throws SAXException {
                log.warn("XML validation warning: {}", exception.getMessage());
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });

        Document document = builder.parse(new File(path));
        return retrievePlayerProps(document);
    }

    private static List<PlayerProperty> retrievePlayerProps(Document document) {
        ArrayList<PlayerProperty> playerProps = new ArrayList<>();

        Element documentElement = document.getDocumentElement();
        NodeList nodes = documentElement.getElementsByTagName(
                PlayerPropsTag.PLAYER_PROPERTY.getTagName());

        for (int i = 0; i < nodes.getLength(); i++) {
            Element item = (Element) nodes.item(i);
            playerProps.add(parsePlayerProperty(item));
        }
        return playerProps;
    }

    private static PlayerProperty parsePlayerProperty(Element item) {
        PlayerProperty playerProp = new PlayerProperty();
        playerProp.setPlayerName(item.getElementsByTagName(PlayerPropsTag.PLAYER_NAME.getTagName())
                .item(0).getTextContent());
        playerProp.setColor(
                Color.valueOf(item.getElementsByTagName(
                        PlayerPropsTag.COLOR.getTagName()).item(0).getTextContent()));
        playerProp.setPlayerType(
                PlayerType.valueOf(item.getElementsByTagName(
                        PlayerPropsTag.PLAYER_TYPE.getTagName()).item(0).getTextContent()));
        return playerProp;
    }

    public static void deletePlayerProps() {
        try {
            Files.deleteIfExists(Path.of(FILENAME));
        } catch (IOException e) {
            log.error("Failed to delete player properties: {}", e.getMessage());
        }
    }
}