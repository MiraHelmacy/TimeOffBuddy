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


import java.security.InvalidParameterException;
import java.time.Duration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;




public class Main {
    static final CommandLineParser parser = new DefaultParser();
    static final Options options = new Options();
    static final HelpFormatter formatter = new HelpFormatter();
    static final String CURRENT_HOURS_SHORT_OPTION = "ch";
    static final String CURRENT_HOURS_LONG_OPTION = "current-hours";
    static final String CURRENT_MINUTES_SHORT_OPTION = "cm";
    static final String CURRENT_MINUTES_LONG_OPTION = "current-minutes";
    static final String EARNED_HOURS_PER_PAY_PERIOD_SHORT_OPTION = "eh";
    static final String EARNED_HOURS_PER_PAY_PERIOD_LONG_OPTION = "earned-hours";
    static final String EARNED_MINUTES_PER_PAY_PERIOD_SHORT_OPTION = "em";
    static final String EARNED_MINUTES_PER_PAY_PERIOD_LONG_OPTION = "earned-minutes";
    static final String TARGET_HOURS_LONG_OPTION = "target";
    static final String TARGET_HOURS_SHORT_OPTION = "t";
    static final String HELP_LONG_OPTION = "help";
    static final String HELP_SHORT_OPTION = "h";

    static final void init(){ 
      //add required options
      options.addOption(EARNED_HOURS_PER_PAY_PERIOD_SHORT_OPTION, EARNED_HOURS_PER_PAY_PERIOD_LONG_OPTION, true, "Hours earned per pay period");
      options.addOption(EARNED_MINUTES_PER_PAY_PERIOD_SHORT_OPTION, EARNED_MINUTES_PER_PAY_PERIOD_LONG_OPTION, true, "Minutes earned per pay period");  
      
      //mark required
      options.getOption(EARNED_HOURS_PER_PAY_PERIOD_SHORT_OPTION).setRequired(true);
      options.getOption(EARNED_MINUTES_PER_PAY_PERIOD_SHORT_OPTION).setRequired(true);

      //add non-required options
      options.addOption(CURRENT_HOURS_SHORT_OPTION, CURRENT_HOURS_LONG_OPTION, true, "Current number of hours earned");
      options.addOption(CURRENT_MINUTES_SHORT_OPTION, CURRENT_MINUTES_LONG_OPTION, true, "Current number of minutes earned");
      options.addOption(TARGET_HOURS_SHORT_OPTION, TARGET_HOURS_LONG_OPTION, true, "Hours of vacation desired to take");
      options.addOption(HELP_SHORT_OPTION, HELP_LONG_OPTION, false, "Print this help message");
        
    }
    
    //print help message with specified footer
    public static final void help(String footer){
      String header = "Calculate the number of pay periods to take your dream vacation!";
      formatter.printHelp("timeoffbuddy", header, options, footer,true);
    }

    //print help message with empty footer.
    public static final void help(){
      help("");
    }

    //validate input all parameters but target must be at least 0 and target must be greater than 0
    public static final void validateInput(int currentHours, int currentMinutes, int earnedHours, int earnedMinutes, int target) throws InvalidParameterException{
      if (currentHours < 0 || currentMinutes < 0 || earnedHours < 0 || earnedMinutes < 0 || target <= 0){
        throw new InvalidParameterException("Invalid Parameter detected. -ch, -cm, -eh, -em must be at least 0 and target must be greater than 0");
      }
    }
    public static void main(String[] args) {
      //set up options  
      init();
        
      //variables used for parsing data
      int currentHours = 0;//current hours of TO
      int currentMinutes = 0;//current minutes of TO
      int earnedHours = 0;//hours of TO earned each pay period
      int earnedMinutes = 0;//minutes of TO earned each pay period
      int targetHours = 0;//number of hours desired to take dream vacation
      
      try{
        CommandLine cmd = parser.parse(options, args);//parse args
        //check if help option is requested
        if (cmd.hasOption(HELP_LONG_OPTION) || cmd.hasOption(HELP_SHORT_OPTION)){
          help();//print help message
          return;
        }
        //required parameters 
        earnedHours = Integer.valueOf(cmd.getOptionValue(EARNED_HOURS_PER_PAY_PERIOD_LONG_OPTION));
        earnedMinutes = Integer.valueOf(cmd.getOptionValue(EARNED_MINUTES_PER_PAY_PERIOD_LONG_OPTION));

        //required parameters with rational default values
        currentHours = Integer.valueOf(cmd.getOptionValue(CURRENT_HOURS_SHORT_OPTION, "0"));
        currentMinutes = Integer.valueOf(cmd.getOptionValue(CURRENT_MINUTES_SHORT_OPTION, "0"));
        targetHours = Integer.valueOf(cmd.getOptionValue(TARGET_HOURS_SHORT_OPTION, "40"));
        
        //validate input
        validateInput(currentHours, currentMinutes, earnedHours, earnedMinutes, targetHours);
      }catch (NumberFormatException e){//earnedHours or earnedMinutes is missing
        help("Missing required parameter");
        System.exit(1);
      }catch (ParseException e){//Cmd Parser failed to parse the args
        help("Unable to Parse Args " + e.getMessage());
        System.exit(2);
      }catch (InvalidParameterException e){//validateInput failed
        help(e.getMessage());
        System.exit(3);
      }catch (Exception e){//unexpected exception occurred
        help("Please contact the developers with the command used and the following stack trace:");
        e.printStackTrace();
        System.exit(4);
      }
      
      
      Duration currentMinutesDuration = Duration.ofMinutes(currentMinutes);//current minutes duration
      currentMinutesDuration = currentMinutesDuration.plus(Duration.ofMinutes(Duration.ofHours(currentHours).toMinutes()));//convert current hours to minutes and add to current minutes

      //perform the same for the earned minutes
      Duration currentEarnedMinutes = Duration.ofMinutes(earnedMinutes);
      currentEarnedMinutes = currentEarnedMinutes.plus(Duration.ofMinutes(Duration.ofHours(earnedHours).toMinutes()));

      //only target hours provided, so do convert hours to minutes
      Duration targetedHoursTotal = Duration.ofMinutes(Duration.ofHours(targetHours).toMinutes());
      

      if (currentEarnedMinutes.toMinutes() == 0){//employee earns no TO D:
        help("No Time Off Earned");
        System.exit(5);
      }

      int payPeriods = 0;//count of pay periods
      while (currentMinutesDuration.toMinutes() < targetedHoursTotal.toMinutes()){//while current minutes is less than target minutes
        currentMinutesDuration = currentMinutesDuration.plus(currentEarnedMinutes);//add earned minutes to current minutes
        payPeriods += 1;//add one to pay period
        //System.out.println("At the End of Pay Period " + payPeriods + " You have " + currentMinutesDuration.toHours() + " Hours of Time Off available. In Minutes: " + currentMinutesDuration.toMinutes());
      }

      System.out.println("Pay Periods to Target: " + payPeriods);//print the result
      
    }
}