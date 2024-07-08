# Time Off Buddy
## Description
A program written in Java that takes in a the current earned hours and minutes of paid timeoff, the amount of time off in hours and minutes awarded each pay period, and a target amount of time off to calculate how many pay periods to reach the target amount of paid time off.

### Compile The Source Code
```
mvn package
```

### Usage
```
java -jar target/timeoffbuddy-1.0.1.jar -eh|--earned-hours <earned hours> -em|--earned-minutes <earned minutes> [-ch|--current-hours <current hours>] [-cm|--current-minutes <current minutes>] [-t|--target <target hours>]
```

## Parameters
- Earned Hours (Required)
The number of hours earned each pay period.
- Earned Minutes (Required)
The number of minutes earned each pay period
- Current Hours (Default 0) 
The number of hours already earned.
- Current Minutes (Default 0) 
The number of minutes already earned.
- Target (Default 40)
The target number of hours.

## Example
You currently have no time off accrued and you want to take a week of vacation. You earn an 4 hours and 37 minutes vacation each pay period.
Current Hours: 0
Current Minutes: 0
Target: 40
Earned Hours: 4
Earned Minutes: 37

```
java -jar target/timeoffbuddy-1.0.1.jar -eh 4 -em 37 -ch 0 -cm 0 -t 40
```

Since Current Hours, Current Minutes and Target are all the defaults, this can be simplified to 

```
java -jar target/timeoffbuddy-1.0.1.jar -eh 4 -em 37
```
