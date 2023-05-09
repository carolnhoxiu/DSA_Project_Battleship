package com.battleship.Interface;

import com.battleship.Board.Ship.ShipType;

public interface Observer{
    void updateSunkShip(ShipType type);
}