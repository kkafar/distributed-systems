import { NullLiteral } from "typescript";
import { WeatherData, WindDirection } from "./types";
import { GeolocationCoordinates } from "./types";
import { GeolocationData } from "./types";

export function mergeWeatherData(data: Array<WeatherData>): WeatherData {
	const result: WeatherData = {
		temperature: 0,
		appliedTemperature: 0,
		clouds: 0,
		pressure: 0,
		humidity: 0,
		sunrise: null,
		sunset: null,
		wind: {
			direction: null,
			direction_n: 0,
			speed: 0,
		},
		precipitation: 0,		
	}

	let temperature_nn = 0
	let appliedTemp_nn = 0
	let clouds_nn = 0
	let pressure_nn = 0
	let humidity_nn = 0
	let windDir_nn = 0
	let windSpeed_nn = 0
	let precipitation_nn = 0;

	data.forEach((sample) => {
		if (sample != null) {
			if (sample.temperature !== null && sample.temperature !== undefined) {
				result.temperature += sample.temperature
				temperature_nn += 1
			}

			if (sample.appliedTemperature !== null && sample.appliedTemperature !== undefined) {
				result.appliedTemperature += sample.appliedTemperature
				appliedTemp_nn += 1
			}

			if (sample.clouds !== null && sample.clouds !== undefined) {
				result.clouds += sample.clouds
				clouds_nn += 1
			}

			if (sample.pressure !== null && sample.pressure !== undefined) {
				result.pressure += sample.pressure
				pressure_nn += 1
			}

			if (sample.humidity !== null && sample.humidity !== undefined) {
				result.humidity += result.humidity
				humidity_nn += 1
			}

			if (sample.wind && sample.wind.direction_n !== null && sample.wind.direction_n !== undefined) {
				result.wind.direction_n += sample.wind.direction_n
				windDir_nn += 1
			}

			if (sample.wind && sample.wind.speed !== null && sample.wind.speed !== undefined) {
				result.wind.speed += sample.wind.speed
				windSpeed_nn += 1
			}

			if (sample.precipitation !== null && sample.precipitation !== undefined) {
				result.precipitation += sample.precipitation
				precipitation_nn += 1
			}

			if (sample.sunrise !== null && sample.sunrise !== undefined) {
				result.sunrise = sample.sunrise
			}

			if (sample.sunset !== null && sample.sunset !== undefined) {
				result.sunset = sample.sunset
			}
		}
	})

	result.temperature /= temperature_nn
	result.appliedTemperature = appliedTemp_nn == 0 ? null : result.appliedTemperature /= appliedTemp_nn
	result.clouds = clouds_nn == 0 ? null : result.clouds /= clouds_nn
	result.pressure = pressure_nn == 0 ? null : result.pressure /= pressure_nn
	result.precipitation = precipitation_nn == 0 ? null : result.precipitation /= precipitation_nn
	result.humidity = humidity_nn == 0 ? null : result.humidity /= humidity_nn
	result.wind.direction_n = windDir_nn == 0 ? null : result.wind.direction_n /= windDir_nn
	result.wind.speed = windSpeed_nn == 0 ? null : result.wind.speed /= windSpeed_nn

	result.wind.direction = resolveWindDir(result.wind.direction_n)


	// const result = data.reduce((res, weatherData) => {
	// 	return {
	// 		temperature: res.temperature + weatherData.temperature,
	// 		appliedTemperature: mergeAppliedTemperature(res.appliedTemperature, weatherData.appliedTemperature),


	// 	}
	// }, {
	// 	temperature: 0,
	// 	appliedTemperature: null,
	// 	clouds: 0,
	// 	pressure: 0,
	// 	humidity: 0,
	// 	sunrise: null,
	// 	sunset: null,
	// 	wind: {
	// 		direction: null,
	// 		direction_n: 0,
	// 		speed: 0,
	// 	},
	// 	precipitation: 0,
	// })
	return result
}

function mergeAppliedTemperature(temp1?: number, temp2?: number): number | null {
	if (temp1 != null && temp2 != null) {
		return (temp1 + temp2) / 2
	} else if (temp1 == null) {
		return temp2
	}
	return null;
}

function resolveWindDir(deg?: number): WindDirection | null {
	if (deg === null) {
		return null
	}

	if ((deg >= 0 && deg < 22.5) || deg >= 337.5) {
		return 'E'
	} else if (deg >= 22.5 && deg < 67.5) {
		return 'NE'
	} else if (deg >= 67.5 && deg < 112.5) {
		return 'N'
	} else if (deg >= 112.5 && deg < 157.5) {
		return 'NW'
	} else if (deg >= 157.5 && deg < 202.5) {
		return 'W'
	} else if (deg >= 202.5 && deg < 247.5) {
		return 'SW'
	} else if (deg >= 247.5 && deg < 292.5) {
		return 'S'
	} else {
		return 'SE'
	}
}
