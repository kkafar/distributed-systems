import sys
import Ice
import Smarthome

def main():
    with Ice.initialize(sys.argv) as communicator:
        base = communicator.stringToProxy("SmartHome:default -p 10000")
        airConditioner = Smarthome.AirConditioning.IAirConditionerPrx.checkedCast(base)

        if not airConditioner:
            raise RuntimeError("Invalid proxy")
        
        airConditioner.setTargetTemp(25)


if __name__ == "__main__":
    main()

