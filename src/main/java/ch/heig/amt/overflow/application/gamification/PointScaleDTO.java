/*
 * AMT : Project 3 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 08.01.2021
 */

package ch.heig.amt.overflow.application.gamification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointScaleDTO {

    private long id;

    private String name;

}
