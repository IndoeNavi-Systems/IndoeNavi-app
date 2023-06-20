package com.indoenavisystems.indoenavi.handlers;

import static org.junit.Assert.*;

import com.indoenavisystems.indoenavi.models.Map;
import com.indoenavisystems.indoenavi.models.SPE;
import com.indoenavisystems.indoenavi.models.TrackedSPE;
import com.indoenavisystems.indoenavi.models.Vec2;

import org.junit.Test;

public class MapHandlerTest {

    @Test
    public void calculateIndoorPosition() {
        //Arrange
        Vec2 expected = new Vec2(483.0,159.0);

        MapHandler mapHandler = MapHandler.getInstance();

        SPE[] spes = {
                new SPE(334.0,17.0,"SPE-D17-A","00:00:00:01"),
                new SPE(632.0,21.0,"SPE-D17-B","00:00:00:02"),
                new SPE(488.0,287.0,"SPE-D17-C","00:00:00:03")};
        Map map = new Map(spes, 0.02);
        mapHandler.setMap(map);

        //Act
        Vec2 result = mapHandler.calculateIndoorPosition(
                new TrackedSPE("00:00:00:01", 4.12),
                new TrackedSPE("00:00:00:02", 4.12),
                new TrackedSPE("00:00:00:03", 7.0));

        //Assert
        assertEquals(expected.getX(), result.getX(), 1);
        assertEquals(expected.getY(), result.getY(), 1);
    }
}