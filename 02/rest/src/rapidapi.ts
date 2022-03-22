import axios, { Axios } from "axios";
import { resolve } from "path";
import { WeatherDataProvider, WeatherData, GeolocationCoordinates, WindDirection } from "./types";

type RapidApiResponse = {
	clouds: number,
	wind_spd: number,
	wind_cdir: WindDirection,
	wind_dir: number,
	sunset?: string,
	sunrise?: string,
	temp: number,
	app_temp: number,
	precip: number,
}

export default class RapidApi implements WeatherDataProvider {
	apiKey: string

	constructor() {
		this.apiKey = process.env.RAPID_WEATHER_API_KEY
	}

	async getWithLocation(coords: GeolocationCoordinates): Promise<WeatherData> {
		const response = await axios.request(this._optionsFactory(coords))
		console.log("RAPID API")
		console.log(response.data)
		if (Array.isArray(response.data.data)) {
			return this._parseResponse(response.data.data[0])
		} else {
			return this._parseResponse(response.data.data)
		}
	}

	_optionsFactory(coords: GeolocationCoordinates): any {
		return {
			method: 'GET',
			url: 'https://weatherbit-v1-mashape.p.rapidapi.com/current',
			params: coords,
			headers: {
				'x-rapidapi-host': 'weatherbit-v1-mashape.p.rapidapi.com',
				'x-rapidapi-key': this.apiKey
			}
		}
	}

	_parseResponse(response: RapidApiResponse): WeatherData {
		console.log(response)
		return {
			temperature: response.temp,
			appliedTemperature: response.app_temp,
			wind: {
				direction: response.wind_cdir,
				direction_n: response.wind_dir,
				speed: response.wind_spd,
			},
			precipitation: response.precip,
			clouds: response.clouds,
		}	
	}
}