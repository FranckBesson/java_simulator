package Broken.JavaSimulator.Utils;

import Broken.JavaSimulator.GameUtils.Coordinate;

/**
 * Created by sebastien on 27/06/17.
 * Used to convert condinate on Map to coordinate on Canvas
 */

public class ConvertPoss {
    public static Coordinate doYouJob(Coordinate mapSize, Coordinate canvasSize, Coordinate toConvert){
        float latitudeRatio =canvasSize.getLatitude()/mapSize.getLatitude();
        float longitudeRatio = canvasSize.getLongitude()/mapSize.getLongitude();
//        System.out.println("Map Size : "+mapSize.getLatitude()+" : "+mapSize.getLongitude()+" CanvasSize: "+canvasSize.getLatitude()+" : "+canvasSize.getLongitude()+" Ratio: "+latitudeRatio+" : "+longitudeRatio);
        return new Coordinate(toConvert.getLatitude()*latitudeRatio,toConvert.getLongitude()*longitudeRatio);

    }
}
