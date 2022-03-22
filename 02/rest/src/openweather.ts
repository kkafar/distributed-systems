import axios, { AxiosResponse } from "axios"
import { GeolocationCoordinates, WeatherDataProvider, WeatherData, WindDirection } from "./types"
import { convertKelvinToCelsius } from "./utils";

type OpenWeatherResponse = {
	coord: GeolocationCoordinates,
	main: {
		temp: number,
		feels_like: number,
		temp_min: number,
		temp_max: number,
		pressure: number,
		humidity: number,
	},
	wind: {
		speed: number,
		deg: number,
	},
	sys: {
		sunrise: number,
		sunset: number,
	},
	clouds: {
		all: number
	}
}


export default class OpenWeather implements WeatherDataProvider {
	apiKey: string

	constructor() {
		this.apiKey = process.env.OPEN_WEATHER_API_KEY
	}

	async getWithLocation(coords: GeolocationCoordinates) {
		const response = await axios.get(`https://api.openweathermap.org/data/2.5/weather?lat=${coords.lat}&lon=${coords.lon}&appid=${this.apiKey}`)
		return this._parseResponse(response.data)
	}

	_parseResponse(response: OpenWeatherResponse): WeatherData {
		return {
			temperature: convertKelvinToCelsius(response.main.temp),
			appliedTemperature: convertKelvinToCelsius(response.main.feels_like),
			pressure: response.main.pressure,
			humidity: response.main.humidity,
			clouds: response.clouds.all,
			sunrise: new Date(response.sys.sunrise * 1000),
			sunset: new Date(response.sys.sunset * 1000),
			wind: {
				direction: this._windDegreeToDirection(response.wind.deg),
				direction_n: response.wind.deg,
				speed: response.wind.speed,
			}
		}
	}

	_windDegreeToDirection(deg?: number): WindDirection {
		// todo
		return "NE"
	}

	
}