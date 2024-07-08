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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class OptionsBuilder implements IOptionsBuilder{
  private Map<String, String> currentOption;
  private List<IOption> options;

  public static final OptionsBuilder optionsBuilderFactory(){
    return new OptionsBuilder();
  }

  OptionsBuilder(){
    resetCurrentOption();
    clearOptions();
  }

  public final void resetCurrentOption(){
    currentOption = Option.optionTemplateMap();
    notRequired();
  }

  public final void clearOptions(){
    options = new ArrayList<>();
  }

  public OptionsBuilder standardOption(){
    currentOption.put(Option.typeKey(), OptionType.STANDARD_OPTION.type());
    return this;
  }

  public OptionsBuilder toggleOption(){
    currentOption.put(Option.typeKey(), OptionType.TOGGLE_OPTION.type());
    return this;
  }

  @Override
  public OptionsBuilder key(String key) {
    currentOption.put(Option.keyKey(), key);
    return this;
  }

  @Override
  public OptionsBuilder shortOption(String shortOption) {
    currentOption.put(Option.shortOptionKey(), shortOption);
    return this;
  }

  @Override
  public OptionsBuilder longOption(String longOption) {
    currentOption.put(Option.longOptionKey(), longOption);
    return this;
  }

  @Override
  public OptionsBuilder required(boolean required) {
    String srequired = "FALSE";
    if (required)srequired = "TRUE";
    currentOption.put(Option.requiredKey(), srequired);
    return this;
  }

  @Override
  public OptionsBuilder required(){
    return required(true);
  }

  @Override
  public OptionsBuilder notRequired() {
    return required(false);
  }

  @Override
  public OptionsBuilder defaultValue(String defaultValue) {
    currentOption.put(Option.defaultValueKey(), defaultValue);
    return this;
  }

  @Override
  public OptionsBuilder confirmOption() {
    IOption newOption = Option.optionFactory(currentOption);
    boolean optionExists = options.contains(newOption);
    if (!optionExists){
      for (IOption option: options){
        if (IOption.optionsMatch(option, newOption)){
          System.err.println("Option Exists");
          optionExists = true;
          break;
        }
      }
    }else {
      System.err.println("Option Exists");
    }
    
    if (!optionExists){
      options.add(newOption);
      resetCurrentOption();
    }

    return this;
  }

  @Override
  public List<IOption> build() {
    return options;
  }

  
}
