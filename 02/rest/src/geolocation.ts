import axios, { AxiosResponse } from "axios";
import { GeolocationData } from "./types";


export default class GeolocationProvider {
	city: string;
	apiKey: string;

	constructor() {
		this.apiKey = process.env.OPEN_WEATHER_API_KEY
	}

	async getByCityName(city: string, countryCode?: string): Promise<GeolocationData> {
		if (countryCode == null) {
			countryCode = "PL"
		}
		const response = await axios.get(`http://api.openweathermap.org/geo/1.0/direct?q=${city},${countryCode}&limit=1&appid=${this.apiKey}`)	
		// console.log("Response from geolocation provider:")
		// console.log(response.data)
		return this._parseAxiosResponse(response);
	}

	_parseAxiosResponse(response?: AxiosResponse): GeolocationData | null {
		if (response == null) {
			return null;
		}

		let responseData = response.data;

		if (Array.isArray(responseData)) {
			responseData = responseData[0]
		}

		return {
			name: responseData.name,
			countryCode: responseData.country,
			state: responseData.state,
			coords: {
				lat: responseData.lat,
				lon: responseData.lon,
			}
		}
	}
}
