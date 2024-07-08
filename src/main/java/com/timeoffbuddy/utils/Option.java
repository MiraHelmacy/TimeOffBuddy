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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Option implements IOption{
  final String shortOption;
  final String longOption;
  final String key;
  final boolean required;
  final String defaultValue;
  final OptionType type;
  final String description;

  static final IOption optionFactory(Map<String, String> optionsMap){
    return new Option(optionsMap);
  }

  static final Map<String, String> optionTemplateMap(){
    List<String> allKeys = allKeys();
    Map<String, String> template = new HashMap<>();
    for (String key: allKeys){
      template.putIfAbsent(key, null);
    }

    return template;
  }

  

  Option(Map<String, String> optionsMap){
    if (!containsAllKeys(optionsMap))throw new InvalidParameterException("Invalid Options Map!");
    shortOption = optionsMap.get(shortOptionKey());
    longOption = optionsMap.get(longOptionKey());
    key = optionsMap.get(keyKey());
    defaultValue = optionsMap.get(defaultValueKey());
    required = Boolean.parseBoolean(optionsMap.get(requiredKey()));
    type = OptionType.whichType(optionsMap.get(typeKey()));
    description = optionsMap.get(descriptionKey());
    if (invalidOption()) throw new InvalidParameterException("Option is invalid");
  }

  static final boolean containsAllKeys(Map<String, String> optionsMap){
    List<String> allKeys = allKeys();
    boolean containsAllKeys = true;
    
    for (String key: allKeys){
      if (optionsMap.containsKey(key))continue;
      containsAllKeys = false;
      break;
    }
    return containsAllKeys;
  }

  static final List<String> allKeys(){
    return Arrays.asList(new String[]{shortOptionKey(), longOptionKey(), keyKey(), requiredKey(), defaultValueKey(), typeKey(), descriptionKey()});
  }



  public static final String shortOptionKey(){
    return "shortOption";
  }

  public static final String longOptionKey(){
    return "longOption";
  }

  public static final String keyKey(){
    return "key";
  }

  public static final String requiredKey(){
    return "required";
  }

  public static final String defaultValueKey(){
    return "default";
  }

  public static final String typeKey(){
    return "type";
  }

  public static final String descriptionKey(){
    return "description";
  }

  @Override
  public String shortOption() {
    return shortOption;
  }

  @Override
  public String longOption() {
    return longOption;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public boolean required() {
    return required;
  }

  @Override
  public String defaultValue() {
    return defaultValue;
  }

  @Override
  public OptionType type() {
    return type;    
  }

  @Override
  public String description() {
    return description;
  }
}
