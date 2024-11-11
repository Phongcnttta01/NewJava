package Chapter4;

public class City {
    private int x,y;
    public  City(int x,int y){
        this.x = x;
        this.y = y;
    }
    public double distanceFrom(City city){
        // Nhận những giá trị x,y khác nhau
        double deltaXSq = Math.pow((city.getX()-this.getX()),2);
        double deltaYSq = Math.pow((city.getY()-this.getY()),2);
        // Tính tiến trình ngắn nhất
        double distance = Math.sqrt(Math.abs(deltaXSq+deltaYSq));
        return distance;
    }
    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
