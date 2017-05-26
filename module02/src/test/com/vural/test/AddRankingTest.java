package com.vural.test;

import com.vural.service.HibernateRankingService;
import com.vural.service.RankingService;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by vural on 20-May-17.
 */
public class AddRankingTest {

    RankingService service = new HibernateRankingService();

    @Test
    public void addRanking(){
        service.addRanking("J. C. Smell", "Drew Lombardo", "Mule", 8);
        assertEquals(service.getRankingFor("J. C. Smell", "Mule"), 8);
    }
}
