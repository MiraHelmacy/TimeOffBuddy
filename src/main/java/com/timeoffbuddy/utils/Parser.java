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

import java.util.List;

public interface Parser {
  void parse(List<String> args, IOptionsBuilder optionsBuilder);
  String get(String key);
  default String help(String applicationName, IOptionsBuilder optionsBuilder){
    
    StringBuffer sb = new StringBuffer(applicationName + ":\n").append("Usage:\n");
    for(IOption option: optionsBuilder.build()){
      String key = option.key();
      String shortOption = option.shortOption();
      String longOption = option.longOption();
      boolean required = option.required();
      String description = option.description();
      OptionType type = option.type();
      String defaultValue = option.defaultValue();
      sb.append("\t")
        .append(key)
        .append(" ")
        .append(description)
        .append(" ");
      
      if (required){
        sb.append(shortOption instanceof String ? (shortOption + "|") : "")
          .append(longOption instanceof String ? longOption : "")
          .append(" ");
      }else {
        sb.append("[")
          .append(shortOption instanceof String ? (shortOption + "|") : "")
          .append(longOption instanceof String ? longOption : "")
          .append("] ");
      }
      if (OptionType.STANDARD_OPTION.type().toString().equals(type.type().toString())){
        sb.append("<" + key + "> ");
      }
      if (defaultValue instanceof String)
      sb.append("Default: ")
        .append(defaultValue);

      sb.append("\n");
    }
    return sb.toString();
  }
}
