module Smarthome {

	exception ExecutionException {
		string description;
	};

	enum DeviceStatus {
		On, Off, Unknown
	};

	struct Time {
		int minutes;
		int hour;
	};

	struct DeviceMetadata {
		string name;
		DeviceStatus status;
		long id;
	};

	interface IDevice {
		idempotent DeviceMetadata getMetadata();
	};

	interface IDeviceStatusController extends IDevice {
		bool turnOn();
		bool turnOff();
		idempotent DeviceStatus getStatus();
	};

	sequence<DeviceMetadata> DeviceList;

	module Controller {
		interface ISmartHomeController {
			DeviceList getDevices();
		};
	};

	module AirConditioning {
		interface IAirConditioner extends IDeviceStatusController {
			void setTargetTemp(float temp) throws ExecutionException;
		}

		interface IZoneAirConditioner extends IAirConditioner {
			void setTargetTempForZone(float temp, int areaId) throws ExecutionException;
		};
		
		interface ITimedAirConditioner extends IAirConditioner {
			void setTargetTempForTime(float temp, Time time) throws ExecutionException;
		};

		interface ISmartAirConditioner extends IZoneAirConditioner, ITimedAirConditioner {
			void turnOnEnergySavingMode();
			void turnOffEnergySavingMode();
			idempotent float getCurrentTemp();
		};
	};

	module Heating {
		interface IWaterHeater extends IDeviceStatusController {
			void setWaterTempForTime(float temp, optional(1) Time time) throws ExecutionException;
		};
	};

	module Kitchen {
		interface IOven extends IDeviceStatusController {
			void preheat(float temp) throws ExecutionException;
		};
	};

	module Lighting {
		interface IBlindsManager extends IDeviceStatusController {
			void draw();
			void undraw();
		};

		interface IPartialDrawBlindsManager extends IDeviceStatusController {
			void drawTo(float percent) throws ExecutionException;
		};
	};
}