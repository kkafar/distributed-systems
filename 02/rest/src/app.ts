import express from "express";
import axios from "axios";

const app = express();
const port = 8080; // default port to listen

type ServerConfig = {
	port: number,
	address: string,
}

const serverConfig: ServerConfig = {
	port: 8080,
	address: "127.0.0.1"
}

app.get("/", (req, res) => {
	res.send("Hello world");

	if (req.socket.remoteAddress) {
		console.log("Incoming connection from " + req.socket.remoteAddress);
	} else {
		console.log("Incoming connection");
	}

});

app.listen(serverConfig.port, () => {
	console.log("Server started at port " + serverConfig.port);
});
