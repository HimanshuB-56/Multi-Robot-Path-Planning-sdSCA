public class Robot {

    double x;
    double y;

    double startX;
    double startY;

    double goalX;
    double goalY;

    double radius;

    public Robot(double x, double y, double goalX, double goalY, double radius) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.goalX = goalX;
        this.goalY = goalY;
        this.radius = radius;
    }
}