import { NumericLiteral } from "typescript"

export interface GeolocationCoordinates {
	lat: number,
	lon: number,
}

export interface GeolocationData {
	name: string,
	coords: GeolocationCoordinates,
	countryCode: string,
	state: string
}

export type WindDirection = 
	| 'N' | 'S' | 'W' | 'E'
	| 'NE' | 'NW' | 'SE' | 'SW'

export interface WeatherData {
	temperature: number,
	appliedTemperature?: number,
	clouds?: number,
	pressure?: number,
	humidity?: number
	sunrise?: Date,
	sunset?: Date,
	wind?: {
		direction?: WindDirection
		direction_n?: number;
		speed: number
	},
	precipitation?: number,
}

export interface WeatherDataProvider {
	getWithLocation: (coords: GeolocationCoordinates) => Promise<WeatherData>
}