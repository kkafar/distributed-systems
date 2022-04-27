import express, { response } from "express";
import bodyParser from "body-parser";
import GeolocationProvider from "./geolocation";
import OpenWeather from "./openweather";
import RapidApi from "./rapidapi";
import { mergeWeatherData } from "./datamanager";

// no types available
const worldMapData = require("city-state-country");

const app = express();
const DEFAULT_PORT = 8080; // default port to listen

type ServerConfig = {
	port: number,
	address: string,
}

const serverConfig: ServerConfig = {
	port: parseInt(process.env.PORT) || DEFAULT_PORT,
	address: "0.0.0.0"
}

const appEndpoints = {
	root: '/',
	geoloacationReturn: '/acquire-geoloaction-by-city-name',
	error: '/error'
}

const geolocationProvider = new GeolocationProvider()
const openweather = new OpenWeather()
const rapidapi = new RapidApi()

app.set('view engine', 'pug')

app.use(bodyParser.urlencoded({extended: false}))
app.use(express.static('resources/html'))
app.use(express.static('resources/pug'))
app.use(function (req, res, next) {
	console.log("Incoming request")
	console.log("URL: " + req.originalUrl)
	console.log("METHOD: " + req.method)
	next()
})

app.get(appEndpoints.root, (req, res) => {
	if (req.socket.remoteAddress) {
		console.log("Incoming request from " + req.socket.remoteAddress);
	}
	res.redirect('/form.html')
});

app.post(appEndpoints.geoloacationReturn, async function(req, res, next) {
	if (res.socket.remoteAddress) {
		console.log("Remote addres information from response socket");
		console.log(res.socket.remoteAddress);
	}

	console.log("Data validation");

	if (!(req.body && req.body.city)) {
		next(Error("Request must contain city name!"))
	}

	const searchResult = worldMapData.searchCity(req.body.city) as Array<any>;

	console.log("Search result")
	console.log(searchResult)

	if (searchResult.length == 0) {
		next(Error("Unrecognized city name. Please check the spelling & do not use any diacritical marks."))
	}

	next()
});

app.post(appEndpoints.geoloacationReturn, async function(req, res, next) {
	console.log("Data fetch")

	const possibleCities = worldMapData.searchCity(req.body.city) as Array<any>
	const countryCode = possibleCities[0].countryShortName

	let geoData;
	try {
		geoData = await geolocationProvider.getByCityName(req.body.city, countryCode)
	} catch (ex) {
		next(Error("Failed to acquire geolocalizaition data. Pleasy try again later."))
	}
	
	console.log("Geolocation data")
	console.log(geoData)

	let openweatherData;
	let rapidData;

	try {
		openweatherData = openweather.getWithLocation(geoData.coords)
		rapidData = rapidapi.getWithLocation(geoData.coords);
	} catch (ex) {
		console.log("Data fetch failed")
		console.log(ex.message)
	}

	if (await openweatherData == undefined && await rapidData == undefined) {
		next(Error("Failed to fetch data from any of the sources. Please try again later."))
	}

	console.log("OpenWeather")
	const openweatherDataAsString = JSON.stringify(await openweatherData)
	console.log(await openweatherData)

	console.log("RapidApi")
	const rapidDataAsString = JSON.stringify(await rapidData)
	console.log(await rapidData)

	const fres = mergeWeatherData([await openweatherData, await rapidData])

	console.log("Computed result")
	console.log(fres);


	res.render('result', {
		title: "Result page",
		temp: `Temperature [deg C]: ${fres.temperature}`,
		appTemp: `Wind chill [deg C]: ${fres.appliedTemperature === null ? "No data" : fres.appliedTemperature}`,
		clouds: `Cloudiness [%]: ${fres.clouds === null ? "No data" : fres.clouds}`,
		pressure: `Pressure [hPa]: ${fres.pressure === null ? "No data" : fres.pressure}`,
		humidity: `Humidity [%]: ${fres.humidity === null ? "No data" : fres.humidity}`,
		windDir: `Wind direction: ${fres.wind.direction === null ? "No data" : fres.wind.direction}`,
		windSpeed: `Wind speed [km/h]: ${fres.wind.speed === null ? "No data" : fres.wind.speed}`,
		precipitation: `Precipitation chance [%]: ${fres.precipitation === null ? "No data" : fres.precipitation}`,
		sunsetTime: `Sunset time: ${fres.sunset === null ? "No data" : fres.sunset}`,
		sunriseTime: `Sunrise time: ${fres.sunrise === null ? "No data" : fres.sunrise}`
	})
})

app.use(async function(err: any, req: any, res: any, next: any) {
	console.log("An error occured: ")
	if (err.message) {
		console.log(err.message)
	} else {
		console.log("No error message provided with error")
	}
	next(err)
})

app.use(async function(err: any, req: any, res: any, next: any) {
	res.render("error", { title: 'Error occured', errorMessage: err.message })
})

app.use(async function(err: any, req: any, res: any, next: any) {
	console.log("Unhandled error occured")
	res.render("error", { title: 'Error occured', errorMessage: err.message })
})

// app.use(async function(err: any, req: any, res: any, next: any) {
	
// })

app.listen(serverConfig.port, () => {
	console.log("Server started at port " + serverConfig.port);
});
