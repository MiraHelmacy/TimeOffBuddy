/*
MIT License

Copyright (c) 2024 Alexander Douglas Helmacy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.timeoffbuddy;

import java.time.Duration;
import java.util.Arrays;

import com.timeoffbuddy.utils.*;
public class Main {
    static final CmdParser parser = CmdParser.CmdParserFactory();
    static final OptionsBuilder ob = OptionsBuilder.optionsBuilderFactory();
    static final String CURRENT_HOURS = "currentHours";
    static final String CURRENT_MINUTES = "currentMinutes";
    static final String EARNED_HOURS = "earnedHours";
    static final String EARNED_MINUTES = "earnedMinutes";
    static final String TARGET_HOURS = "targetHours";
    static final String HELP = "help";

    static final void init(){ 
        ob.standardOption()
          .key(CURRENT_HOURS)
          .shortOption("-ch")
          .longOption("--current-hours")
          .required()
          .description("Current Hours Accrued.")
          .defaultValue("0")
          .confirmOption()
          .standardOption()
          .key(CURRENT_MINUTES)
          .shortOption("-cm")
          .longOption("--current-minutes")
          .description("Current Minutes Accrued")
          .required()
          .defaultValue("0")
          .confirmOption()
          .standardOption()
          .key(EARNED_HOURS)
          .shortOption("-eh")
          .longOption("--earned-hours")
          .description("Hours Earned Per Pay Period")
          .required()
          .confirmOption()
          .standardOption()
          .key(EARNED_MINUTES)
          .shortOption("-em")
          .longOption("--earned-minutes")
          .description("Minutes Earned Per Pay Period")
          .required()
          .confirmOption()
          .standardOption()
          .key(TARGET_HOURS)
          .shortOption("-t")
          .longOption("--target")
          .description("Desired Hours")
          .required()
          .defaultValue("40")
          .confirmOption()
          .toggleOption()
          .key(HELP)
          .shortOption("-h")
          .longOption("--help")
          .description("Prints this Help Message")
          .notRequired()
          .confirmOption();

    }

    static void help(){
      System.out.println(parser.help("Time Off Buddy", ob));
    }
    public static void main(String[] args) {
        init();
        try{
          parser.parse(Arrays.asList(args), ob);
        }catch (Exception e){
          System.err.println("Failed to Parse Args " + e.getMessage());
          help();
          System.exit(1);
        }
        
        
        boolean help = Boolean.parseBoolean(parser.get(HELP));

        if (help){
          help();
          System.exit(0);
        }
        int currentHours = 0;
        int currentMinutes = 0;
        int earnedHours = 0;
        int earnedMinutes = 0;
        int targetHours = 0;
        try{
          currentHours = Integer.parseInt(parser.get(CURRENT_HOURS) instanceof String ? parser.get(CURRENT_HOURS) : "");
          currentMinutes = Integer.parseInt(parser.get(CURRENT_MINUTES) instanceof String ? parser.get(CURRENT_MINUTES) : "");
          earnedHours = Integer.parseInt(parser.get(EARNED_HOURS) instanceof String ? parser.get(EARNED_HOURS) : "");
          earnedMinutes = Integer.parseInt(parser.get(EARNED_MINUTES) instanceof String ? parser.get(EARNED_MINUTES) : "");
          targetHours = Integer.parseInt(parser.get(TARGET_HOURS) instanceof String ? parser.get(TARGET_HOURS) : "");
        }catch (Exception e){
          System.err.println("Missing Required parameter");
          help();
          System.exit(2);
        }

        Duration currentMinutesDuration = Duration.ofMinutes(currentMinutes);
        currentMinutesDuration = currentMinutesDuration.plus(Duration.ofMinutes(Duration.ofHours(currentHours).toMinutes()));

        Duration currentEarnedMinutes = Duration.ofMinutes(earnedMinutes);
        currentEarnedMinutes = currentEarnedMinutes.plus(Duration.ofMinutes(Duration.ofHours(earnedHours).toMinutes()));

        Duration targetedHoursTotal = Duration.ofMinutes(Duration.ofHours(targetHours).toMinutes());
        
        if (currentEarnedMinutes.toMinutes() == 0){
          System.err.println("No Time Off Earned");
          return;
        }


        int payPeriods = 0;
        while (currentMinutesDuration.toMinutes() < targetedHoursTotal.toMinutes()){
          currentMinutesDuration = currentMinutesDuration.plus(currentEarnedMinutes);
          payPeriods += 1;
          //System.out.println("At the End of Pay Period " + payPeriods + " You have " + currentMinutesDuration.toHours() + " Hours of Time Off available. In Minutes: " + currentMinutesDuration.toMinutes());
        }

        System.out.println("Pay Periods to Target: " + payPeriods);
    }
}