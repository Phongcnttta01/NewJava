package Chapter4;

public class Route {
    private City[] route;
    private double distance = 0;
    public Route(Individual individual, City cities[]){
        // Lấy nhiễm sắc thể của từng cá thể
        int chromosome[] = individual.getChromosome();
        // Tạo đường đi
        this.route = new City[cities.length];
        for(int geneIndex = 0; geneIndex < chromosome.length; ++geneIndex){
            this.route[geneIndex] = cities[chromosome[geneIndex]];
        }
    }
    public double getDistance(){
        if(this.distance > 0){
            return this.distance;
        }
        // Vòng lặp lấy đường đi của  các thành phố và tính toán
        double totalDistance = 0;
        for(int cityIndex =0;cityIndex+1 < this.route.length; ++cityIndex){
            totalDistance += this.route[cityIndex].distanceFrom(this.route[cityIndex+1]);
        }
        totalDistance += this.route[this.route.length - 1].distanceFrom(this.route[0]);
        this.distance = totalDistance;

        return totalDistance;
    }
}
