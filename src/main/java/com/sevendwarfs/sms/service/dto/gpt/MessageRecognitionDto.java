package com.sevendwarfs.sms.service.dto.gpt;

public record MessageRecognitionDto(
    Boolean linguisticDerailment,
    Boolean delusions,
    Boolean hallucination,
    Boolean disorganizedLanguage,
    String rationale
) {

  @Override
  public String toString() {
    return "MessageRecognitionDto{" +
        "언어의 탈선=" + linguisticDerailment +
        ", 환각=" + delusions +
        ", 망상=" + hallucination +
        ", 와해된 언어=" + disorganizedLanguage +
        ", 근거='" + rationale + '\'' +
        '}';
  }
}
