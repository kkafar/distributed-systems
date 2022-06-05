import os
import sys
import Ice
import Smarthome
import json


def main():
    with Ice.initialize(sys.argv) as communicator:
        config_dir = os.environ.get("CONFIG_DIR")
        print(config_dir)
        
        
        base = communicator.stringToProxy("AirConditioner:default -p 10000")
        base_controller = communicator.stringToProxy("Controller:default -p 10000")
        air_conditioner = Smarthome.AirConditioning.IAirConditionerPrx.checkedCast(base)
        controller = Smarthome.Controller.ISmartHomeControllerPrx.checkedCast(base_controller)


        if not air_conditioner:
            raise RuntimeError("Invalid proxy")
        
        air_conditioner.setTargetTemp(-100)

        devices = controller.getDevices()
        
        print(devices)
        print(type(devices[0]))
        print(type(devices[0].status))


if __name__ == "__main__":
    main()

