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

public interface IOption {
  String shortOption();
  String longOption();
  String key();
  boolean required();
  String defaultValue();
  OptionType type();
  
  static boolean optionsMatch(IOption opt1, IOption opt2){
    String opt1Key = opt1.key();
    String opt1ShortOption = opt1.shortOption();
    String opt1LongOption = opt1.longOption();

    String opt2Key = opt2.key();
    String opt2ShortOption = opt2.shortOption();
    String opt2LongOption = opt2.longOption();

    boolean keysMatch = opt1Key.equals(opt2Key);
    boolean shortOptionsMatch = opt1ShortOption.equals(opt2ShortOption);
    boolean longOptionsMatch = opt1LongOption.equals(opt2LongOption);
    
    return keysMatch || shortOptionsMatch || longOptionsMatch;
  }

  default boolean invalidOption(){
    String shortOption = shortOption();
    String longOption = longOption();
    String key = key();
    boolean invalidKey = key instanceof String && key.trim().isEmpty();
    boolean invalidOptionSpecifier = shortOption instanceof String && longOption instanceof String && shortOption.trim().isEmpty() && longOption.trim().isEmpty();
    return invalidKey || invalidOptionSpecifier;
  }

  default boolean validOption(){
    return !invalidOption();
  }
}
