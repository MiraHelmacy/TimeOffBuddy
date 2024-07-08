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
package com.timeoffbuddy.utils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CmdParser implements Parser{
  
  public static final CmdParser CmdParserFactory(){
    return new CmdParser();
  }

  HashMap<String, String> parameters;
  
  private final void clearParameters(){
    parameters = new HashMap<>();
  }

  CmdParser(){
    clearParameters();
  }

  @Override
  public void parse(List<String> args, IOptionsBuilder optionsBuilder) {
    clearParameters();
    List<IOption> options = optionsBuilder.build();//list of options built by options builder
    HashMap<String, IOption> optionStringToOption = new HashMap<>();//map of short and long option to Option Object to make lookup when parsing faster
    
    for (IOption option: options){
      //get short option and set to empty string if null
      String shortOption = option.shortOption();
      shortOption = shortOption instanceof String ? shortOption : "";
      
      //do the same for long option as described for short option above
      String longOption = option.longOption();
      longOption = longOption instanceof String ? longOption : "";

      //map short and long option to corresponding option as applicable to make 
      if (!shortOption.isEmpty()) optionStringToOption.putIfAbsent(shortOption, option);
      if (!longOption.isEmpty()) optionStringToOption.putIfAbsent(longOption, option);
    
      String key = option.key();//get the option key
      
      //get the default value of any option
      String defaultValue = option.defaultValue();

      //if the default value is not null
      if (defaultValue instanceof String){
        //add default value and key to parameters
        parameters.put(key, defaultValue);
      }
    }

    //actually parse the args
    try{
      Iterator<String> argsIterator = args.iterator();
      while(argsIterator.hasNext()){
        String currentArg = argsIterator.next();
        IOption currentOption = optionStringToOption.get(currentArg);
        if (!(currentOption instanceof IOption)) throw new Exception("Unknown Option: " + currentArg);
        OptionType currentOptionType = currentOption.type();
        String key = currentOption.key();
        String data;
        switch (currentOptionType){
          case STANDARD_OPTION:
            String rawData = argsIterator.next();
            data = rawData;
            break;
          case TOGGLE_OPTION:
            data = "TRUE";
            break;
          default:
            throw new UnsupportedOperationException("Option Type Not Supported");
        }
        parameters.put(key, data);
      }
    }catch (Exception e){
      throw new InvalidParameterException(e.getMessage());
    }
    
  }
  @Override
  public String get(String key) {
    return parameters.get(key);
  }

}
