package com.vural.service;

import com.vural.model.Person;
import org.hibernate.Session;

import java.util.Map;

/**
 * Created by vural on 19-May-17.
 */
public interface RankingService {

    int getRankingFor(String subject, String skill);

    void addRanking(String subject, String observer, String skill, int ranking);

    void updateRanking(String subject, String observer, String skill, int ranking);

    void removeRanking(String subject, String observer, String skill);

    Person findBestPersonFor(String skill);

    Map<String, Integer> findRankingsFor(String subject);
}
