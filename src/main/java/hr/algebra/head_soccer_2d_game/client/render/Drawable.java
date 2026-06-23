package hr.algebra.head_soccer_2d_game.client.render;


import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;

public interface Drawable{
    void draw(GameDataSnapshot gameDataSnapshot);
}