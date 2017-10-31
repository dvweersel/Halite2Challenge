import hlt.*;

import java.util.ArrayList;

public class StarterBot {

    public static void main(final String[] args) {
        final Networking networking = new Networking();
        final GameMap gameMap = networking.initialize("StarterBot");

        final ArrayList<Move> moveList = new ArrayList<>();
        for (;;) {
            moveList.clear();
            gameMap.updateMap(Networking.readLineIntoMetadata());

            for (final Ship ship : gameMap.getMyPlayer().getShips().values()) {
                if (ship.getDockingStatus() != Ship.DockingStatus.Undocked) {
                    continue;
                }

                for (final Planet planet : gameMap.getAllPlanets().values()) {
                    if (planet.isOwned()) {
                        continue;
                    }

                    if (ship.canDock(planet)) {
                        moveList.add(new DockMove(ship, planet));
                        break;
                    }

                    final ThrustMove newThrustMove = Navigation.navigateShipToDock(gameMap, ship, planet, Constants.MAX_SPEED);
                    if (newThrustMove != null) {
                        moveList.add(newThrustMove);
                    }

                    break;
                }
            }
            Networking.sendMoves(moveList);
        }
    }
}
