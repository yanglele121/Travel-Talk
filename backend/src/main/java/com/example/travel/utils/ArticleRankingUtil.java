package com.example.travel.utils;

/**
 * @author 杨乐乐
 * @time 2023-05-26 10:56
 */
import java.time.Duration;
import java.time.LocalDateTime;

public class ArticleRankingUtil {

    public static double timeDecay(long timeDiff, long halfLife) {
        return Math.exp(-Math.log(2) * timeDiff / halfLife);
    }

    public static double calculateScore(int likes, int comments, int favorites, int views, LocalDateTime publishTime) {
        // 假设权重为1
        double weightLikes = 2.0;
        double weightComments = 1.5;
        double weightFavorites = 1.0;
        double weightViews = 0.5;

        // 计算时间差（假设以小时为单位）
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(publishTime, currentTime);
        long timeDiff = duration.toHours();

        // 计算时间衰减因子
        double decayFactor = timeDecay(timeDiff, 24); // 假设半衰期为24小时

        // 计算综合评分
        double score = (weightLikes * likes +
                weightComments * comments +
                weightFavorites * favorites +
                weightViews * views) * decayFactor;

        return score;
    }
}
