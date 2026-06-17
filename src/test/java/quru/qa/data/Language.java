package quru.qa.data;

public enum Language {
  BY("Беларуская"),
  RU("Русский"),
  EN("English");

  public final String description;

  Language(String description) {
    this.description = description;
  }
}
