package com.example.productivitytracker;

import com.example.productivitytracker.models.Event;
import com.example.productivitytracker.models.IdealData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecommendationsService
{
    private final List<Event> events;
    private final List<String> recommendations = new ArrayList<>();
    private final String[] eventType = {"Study", "Entertainment", "Gym", "Others"};
    private final float[] eventDuration = new float[4];
    private final float[] eventPercentage = new float[4];

    float prodHours;
    float prodScore;
    IdealData idealData;

    RecommendationsService(List<Event> eventList, IdealData idealData){
        this.events = eventList;
        this.idealData = idealData;
        generateRecommendations();

    }

    public List<String> getRecommendations(){
        return recommendations;
    }

    private void generateRecommendations(){
        calculateProductiveHours();
        calculateProdScore();
        recommendations.clear();

        if(prodScore < 0.4) recommendations.add("Your productivity is too low today. Try doing some productive work.");
        if(eventDuration[2] < idealData.workout_hours) recommendations.add("You have spent less time in gym today, than your goal. Try to burn some calories!");
        else if(eventPercentage[2] < 0.05) recommendations.add("Your workout hours are too less. Regular physical activity can improve your muscle strength and boost your endurance. Time to do some exercise!");

        if(eventDuration[1] > idealData.screen_time) recommendations.add("You have spent more time on Entertainment than your expected goal. Time to relax your eyes ");
        else if(eventPercentage[1] > 0.4) recommendations.add("You have spent too much time in Entertainment. Time to relax your eyes and be more productive!");
    }

    private void calculateProdScore(){
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        prodScore = prodHours / (float)hours;
    }

    private void calculateProductiveHours()
    {
        for(int j = 0;j < 4; j++){
            eventDuration[j] = 0.0f;
            eventPercentage[j] = 0.0f;
        }

        float productiveDuration = 0.0f;
        float totalDuration = 0.0f;
        for(int i = 0;events != null && i < events.size(); i++)
        {
            long startTime = events.get(i).start_time;
            long endTime = events.get(i).end_time;
            float duration = (float) (endTime - startTime) / 3600.0f;
            totalDuration += duration;

            if(events.get(i).type.equalsIgnoreCase(eventType[0]) || events.get(i).type.equalsIgnoreCase(eventType[2])){
                productiveDuration += duration;
            }

            for(int j = 0;j < 4; j++){
                if(events.get(i).type.equalsIgnoreCase(eventType[j])){
                    eventDuration[j] += duration;
                }
            }
        }

        for(int j = 0;j < 4; j++) eventPercentage[j] = eventDuration[j] / totalDuration;
        prodHours = productiveDuration;
    }
}
