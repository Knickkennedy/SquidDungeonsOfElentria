package roguelike.Generation;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import roguelike.utilities.Point;
import roguelike.utilities.Roll;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter @Setter
public class Map_Builder {

    private Tile[][] map;
    private char[][] pathfinding;

    private Point stairsUp;
    private Point stairsDown;

    private List <Point> frontier = new ArrayList<>();
    private List <Point> deadEnds = new ArrayList<>();
    private List <Point> potentialDoors = new ArrayList<>();
    private List <Point> extraDoors = new ArrayList<>();
    private List <Point> connections = new ArrayList<>();
    private List <Point> ctr = new ArrayList<>();
    private List <Room> rooms = new ArrayList<>();

    private int maxRoomSize;
    private int minRoomSize;
    private int numberOfPlacementTries;

    private JSONObject tile_file;

    private boolean[][] roomFlag;
    private boolean[][] connected;
    private boolean[][] revealed;

    public Map_Builder(int width, int height){
        this.pathfinding = new char[width][height];
        this.map = new Tile[width][height];
        this.minRoomSize = 3;
        this.maxRoomSize = 7;
        this.numberOfPlacementTries = 100;
        this.connected = new boolean[width][height];
        this.roomFlag = new boolean[width][height];
        this.revealed = new boolean[width][height];
		JSONParser parser = new JSONParser();
	    try {
		    this.tile_file = (JSONObject)parser.parse(new FileReader("assets/tiles.txt"));
	    } catch (IOException | ParseException e) {
		    e.printStackTrace();
	    }
    }

    public void buildStandardLevel(){
        initializeMap();
        placeRooms();
        startMaze();
        findConnections();
        placeAllDoors();
        removeAllDeadEnds();
        placeStairs();
        initializePathfinding();
    }

    public void build_final_level(){
	    initializeMap();
	    placeRooms();
	    startMaze();
	    findConnections();
	    placeAllDoors();
	    removeAllDeadEnds();
	    place_only_up_stairs();
	    initializePathfinding();
    }

    private void initializeMap(){

        JSONObject wall = (JSONObject)tile_file.get("wall");

        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[0].length; y++){
                map[x][y] = new Tile(wall);
                connected[x][y] = false;
                revealed[x][y] = false;
            }
        }
    }

    private void placeRooms(){
        for(int i = 0; i < numberOfPlacementTries; i++){
            placeRoom();
        }
    }

    private void placeRoom(){
        int h = Roll.rand(minRoomSize, maxRoomSize);
        if(h % 2 == 0){
            h = h + 1;
        }
        int w = Roll.rand(h, maxRoomSize);
        if(w % 2 == 0){
            w = w + 1;
        }
        int x = Roll.rand(0, (map.length - w - 2));
        int y = Roll.rand(0, (map[0].length - h - 2));
        if(x % 2 == 0){
            x += 1;
        }
        if(y % 2 == 0){
            y += 1;
        }

        Room newRoom = new Room(x, y, w, h);

        boolean failed = false;
        for(Room otherRoom : rooms){
            if(newRoom.intersects(otherRoom)){
                failed = true;
                break;
            }
        }
        if(!failed){
            stampRoom(newRoom);
            rooms.add(newRoom);
        }
    }

    private void stampRoom(Room room){

    	JSONObject floor = (JSONObject)tile_file.get("floor - dungeon");

        for(int i = 0; i <= room.getBottomRight().x - room.getTopLeft().x; i++){
            for(int j = 0; j <= room.getBottomRight().y - room.getTopLeft().y; j++){
                map[room.getTopLeft().x + i][room.getTopLeft().y + j] = new Tile(floor);
                roomFlag[room.getTopLeft().x + i][room.getTopLeft().y + j] = true;
            }
        }
    }

    private void startMaze(){
        for(int i = 1; i < map.length - 2; i++){
            for(int j = 1; j < map[0].length - 2; j++){
                if(isSolid(i, j)){
                    generateMaze(i, j);
                }
            }
        }
    }

    private boolean isSolid(int x, int y){
        return (map[x][y].sprite.character == '#')
                && (map[x + 1][y].sprite.character =='#')
                && (map[x - 1][y].sprite.character == '#')
                && (map[x][y - 1].sprite.character == '#')
                && (map[x][y + 1].sprite.character == '#')
                && (map[x + 1][y + 1].sprite.character == '#')
                && (map[x + 1][y - 1].sprite.character == '#')
                && (map[x - 1][y + 1].sprite.character == '#')
                && (map[x - 1][y - 1].sprite.character == '#');
    }

    private void generateMaze(int x, int y){
        Point start = new Point(x, y);
        buildFrontier(start);
        carvePath(start);
        updateFrontier();
        while(!frontier.isEmpty()){
            Point current = frontier.remove(Roll.rand(0, frontier.size() - 1));
            buildFrontier(current);
            carvePath(current);
            updateFrontier();
        }
    }

    private void buildFrontier(Point p){
        for(Point direction : Point.cardinal){
            if(isInBounds(p.getNeighbor(direction))){
                if(isDirectionallySolid(p, direction)){
                    frontier.add(p.getNeighbor(direction));
                }
            }
        }
    }

    private void carvePath(Point s){
    	JSONObject floor = (JSONObject)tile_file.get("floor - dungeon");
        map[s.x][s.y] = new Tile(floor);
    }

    private void updateFrontier(){
        List <Point> toRemove = new ArrayList<>();
        for(Point p : frontier){
            if(!isValidMazeLocation(p)){
                toRemove.add(p);
            }
        }
        frontier.removeAll(toRemove);
    }

    private void findConnections(){
        for(int i = 1; i < map.length - 1; i++){
            for(int j = 1; j < map[0].length - 1; j++){
                if((map[i][j].sprite.character == '#')
                        && (map[i - 1][j].sprite.character == '.')
                        && (map[i + 1][j].sprite.character == '.')
                        && (roomFlag[i + 1][j] || roomFlag[i - 1][j])){
                    connections.add(new Point(i, j));
                }
                if((map[i][j].sprite.character == '#')
                        && (map[i][j - 1].sprite.character == '.')
                        && (map[i][j + 1].sprite.character == '.')
                        && (roomFlag[i][j - 1] || roomFlag[i][j + 1])){
                    connections.add(new Point(i, j));
                }
            }
        }
    }

    private void placeAllDoors(){
        Room tempRoom = rooms.get(Roll.rand(0, rooms.size() - 1));
        floodFill(tempRoom.getTopLeft().x, tempRoom.getTopLeft().y);
        while(!connections.isEmpty()){
            findDoors();
            placeDoor();
            createExtraDoors();
            removeExtraConnectors();
        }
    }

    private void findDoors(){
        Collections.shuffle(connections);
        for(Point p : connections){
            if((connected[p.x - 1][p.y]) && (!connected[p.x + 1][p.y])){
                potentialDoors.add(p);
            }
            if((connected[p.x + 1][p.y]) && (!connected[p.x - 1][p.y])){
                potentialDoors.add(p);
            }
            if((connected[p.x][p.y - 1]) && (!connected[p.x][p.y + 1])){
                potentialDoors.add(p);
            }
            if((connected[p.x][p.y + 1]) && (!connected[p.x][p.y - 1])){
                potentialDoors.add(p);
            }
        }
    }

    private void placeDoor(){
        Point door = potentialDoors.get(Roll.rand(0, potentialDoors.size() - 1));
        while (hasDoorNeighbor(door)) {
            door = potentialDoors.get(Roll.rand(0, potentialDoors.size() - 1));
        }

        JSONObject doorobj = (JSONObject)tile_file.get("door - closed");

        map[door.x][door.y] = new Tile(doorobj);
        floodFill(door.x, door.y);
        extraDoors.addAll(potentialDoors);
        potentialDoors.clear();
    }

    private void createExtraDoors(){
        Collections.shuffle(extraDoors);
        for(int i = 0; i < Roll.rand(1, 3); i++){
            Point check = extraDoors.get(Roll.rand(0, extraDoors.size() - 1));
            if(!hasDoorNeighbor(check)){
            	JSONObject door_closed = (JSONObject)tile_file.get("door - closed");
                map[check.x][check.y] = new Tile(door_closed);
            }
        }
        extraDoors.clear();
    }
    private void removeExtraConnectors(){
        for(Point p : connections){
            if(connected[p.x - 1][p.y] && connected[p.x + 1][p.y]){
                ctr.add(p);
            }
            if(connected[p.x][p.y - 1] && connected[p.x][p.y + 1]){
                ctr.add(p);
            }
        }
        connections.removeAll(ctr);
        ctr.clear();
    }

    private void placeStairs(){
        Collections.shuffle(rooms);
        Room upstairs = rooms.get(0);
        Room downstairs = rooms.get(rooms.size() - 1);
        int x1 = Roll.rand(upstairs.getTopLeft().x + 1, upstairs.getBottomRight().x - 1);
        int y1 = Roll.rand(upstairs.getTopLeft().y + 1, upstairs.getBottomRight().y - 1);
        int x2 = Roll.rand(downstairs.getTopLeft().x + 1, downstairs.getBottomRight().x - 1);
        int y2 = Roll.rand(downstairs.getTopLeft().y + 1, downstairs.getBottomRight().y - 1);
        JSONObject stairs_up = (JSONObject)tile_file.get("stairs - up");
        JSONObject stairs_down = (JSONObject)tile_file.get("stairs - down");
        map[x1][y1] = new Tile(stairs_up);
        map[x2][y2] = new Tile(stairs_down);
        stairsUp = new Point(x1, y1);
        stairsDown = new Point(x2, y2);
    }

    private void place_only_up_stairs(){
	    Collections.shuffle(rooms);
	    Room upstairs = rooms.get(0);
	    int x1 = Roll.rand(upstairs.getTopLeft().x + 1, upstairs.getBottomRight().x - 1);
	    int y1 = Roll.rand(upstairs.getTopLeft().y + 1, upstairs.getBottomRight().y - 1);

	    JSONObject stairs_up = (JSONObject)tile_file.get("stairs - up");
	    map[x1][y1] = new Tile(stairs_up);
	    stairsUp = new Point(x1, y1);

    }

    private void initializePathfinding(){
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[0].length; y++){
                pathfinding[x][y] = map[x][y].passable ? '.' : '#';
            }
        }
    }

    private boolean hasDoorNeighbor(Point p){
        for(Point direction : Point.cardinal){
            if(getTile(p.getNeighbor(direction)).sprite.character == '+') return true;
        }
        return false;
    }

    private void floodFill(int x, int y){
        if(((map[x][y].sprite.character == '.') || (map[x][y].sprite.character == '+')) && (!connected[x][y])){
            connected[x][y] = true;
        }
        else{
            return;
        }
        floodFill(x + 1, y);
        floodFill(x - 1, y);
        floodFill(x, y + 1);
        floodFill(x, y - 1);
    }

    private Tile getTile(Point p){
        return map[p.x][p.y];
    }

    private boolean isInBounds(Point p){
        return isHorizontallyInBounds(p.x) && isVerticallyInBounds(p.y);
    }

    private boolean isInBounds(int x, int y){
        return isHorizontallyInBounds(x) && isVerticallyInBounds(y);
    }

    private boolean isHorizontallyInBounds(int x){
        return x > 0 && x < map.length - 1;
    }

    private boolean isVerticallyInBounds(int y){
        return y > 0 && y < map[0].length - 1;
    }

    private boolean isDirectionallySolid(Point p, Point direction){
        List <Point> directionalNeighbors = p.getDirectionalNeighbors(direction);
        for(Point toCheck : directionalNeighbors){
            if(isInBounds(toCheck) && (getTile(toCheck).sprite.character != '#')){
                return false;
            }
        }
        return true;
    }

    private boolean isValidMazeLocation(Point p){
        List <Point> neighbors = p.getFrontierNeighbors(p.directionFromParent);
        int floorCount = 0;
        for(Point point : neighbors){
            if(isInBounds(point)) {
                if (getTile(point).sprite.character == '.') {
                    floorCount++;
                }
            }
        }
        return floorCount == 0;
    }

    private void removeAllDeadEnds(){
        for(int i = 0; i < 100; i++){
            removeDeadEnds();
        }
    }

    private void removeDeadEnds(){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                int wallCount = 0;
                if(map[i][j].sprite.character == '.' || map[i][j].sprite.character == '+'){
                    if(map[i - 1][j].sprite.character == '#'){
                        wallCount++;
                    }
                    if(map[i + 1][j].sprite.character == '#'){
                        wallCount++;
                    }
                    if(map[i][j - 1].sprite.character == '#'){
                        wallCount++;
                    }
                    if(map[i][j + 1].sprite.character == '#'){
                        wallCount++;
                    }
                    if(wallCount >= 3){
                        Point newP = new Point(i, j);
                        deadEnds.add(newP);
                    }
                }
            }
        }
        JSONObject wall = (JSONObject)tile_file.get("wall");
        for(Point p : deadEnds){
            map[p.x][p.y] = new Tile(wall);
        }
        deadEnds.clear();
    }
}
