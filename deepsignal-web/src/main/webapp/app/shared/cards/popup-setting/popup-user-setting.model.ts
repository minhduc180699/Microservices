export class UserSettingModel {
  private weather;
  private stock;
  private locWeather;

  constructor(temperatureUnit?, cardStyle?, locWeather?) {
    if (temperatureUnit) {
      this.weather = { showWeatherCard: true, alwaysDetectLocation: true, temperatureUnit: temperatureUnit };
    }
    if (cardStyle) {
      this.stock = { showStockCard: true, cardStyle: cardStyle };
    }
    if (locWeather) this.locWeather = locWeather;
  }
}
