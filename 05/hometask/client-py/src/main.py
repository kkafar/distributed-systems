import os
import sys
import Ice
import Smarthome
import json

CONST_CONFIG_DIR = "CONFIG_DIR"

AIR_CONDITIONER = "air-conditioner"
ZONE_AIR_CONDITIONER = 'zone-air-conditioner'
TIMED_AIR_CONDITIONER = 'timed-air-conditioner'
SMART_AIR_CONDITIONER = 'smart-air-conditioner'
WATER_HEATER = 'water-heater'
OVEN = 'oven'
BLINDS_MANAGER = 'blinds-manager'
PARTIAL_DRAW_BLINDS_MANAGER = 'partial-draw-blinds-manager'
CONTROLLER = 'controller'

map_obj_type_to_calls = {
    'device': [
        'turnOn',
        'turnOff',
        'getStatus'
    ],
    AIR_CONDITIONER: [
        'setTargetTemp(float temp)',
        'getCurrentTemp'
    ],
    ZONE_AIR_CONDITIONER: [
        'setTargetTemp(float temp)',
        'getCurrentTemp',
        'setTargetTempForZone(float temp, int zoneid)'
    ],
    TIMED_AIR_CONDITIONER: [
        'setTargetTemp(float temp)',
        'getCurrentTemp',
        'setTargetTempForTime(float temp, int h, int m)'
    ],
    SMART_AIR_CONDITIONER: [
        'setTargetTemp(float temp)',
        'getCurrentTemp',
        'turnOnEnergySavingMode',
        'turnOffEnergySavingMode'
    ],
    WATER_HEATER: [
        'setWaterTempForTime(float temp)',
        'setWaterTempForTime(float temp, int h, int m)',
        'getCurrentTemp'
    ],
    OVEN: [
        'preheat(float temp)',
        
    ],
    BLINDS_MANAGER: [
        'draw',
        'undraw',
        'isDrawn'
    ],
    PARTIAL_DRAW_BLINDS_MANAGER: [
        'drawTo(float percentage)',
        "getDrawLevel"
    ],
}

map_obj_type_to_proxy = {
    AIR_CONDITIONER: Smarthome.AirConditioning.IAirConditionerPrx,
    ZONE_AIR_CONDITIONER: Smarthome.AirConditioning.IZoneAirConditionerPrx,
    TIMED_AIR_CONDITIONER: Smarthome.AirConditioning.ITimedAirConditionerPrx,
    SMART_AIR_CONDITIONER: Smarthome.AirConditioning.ISmartAirConditionerPrx,
    WATER_HEATER: Smarthome.Heating.IWaterHeaterPrx,
    OVEN: Smarthome.Kitchen.IOvenPrx,
    BLINDS_MANAGER: Smarthome.Lighting.IBlindsManagerPrx,
    PARTIAL_DRAW_BLINDS_MANAGER: Smarthome.Lighting.IPartialDrawBlindsManagerPrx
}



def handle_method_call(communicator, object: dict, method_name: str, port: int):
    try:
        base = communicator.stringToProxy(get_proxy_string(object['name'], port))
        device_proxy = map_obj_type_to_proxy[object['type']].checkedCast(base)
        
        method_impl = getattr(device_proxy, method_name)

        if method_impl is None:
            print("Failed to find method with name:", method_name)
            return None

        args = []
        args.append(input_float())
        args.append(input_int())
        args.append(input_time())

        args = [arg for arg in args if arg is not None]

        return method_impl(*args)

    except Exception as ex:
        print("Failed to handle method call")
        print(ex)


def input_float() -> float:
    try:
        return float(input("(float)>> "))
    except:
        return None

def input_time() -> Smarthome.Time:
    try:
        hours = int(input("(hour)>> "))
        minutes = int(input("(minutes)>> "))
        return Smarthome.Time(hours, minutes)
    except:
        return None

def input_int() -> int:
    try:
        return int(input("(int)>> "))
    except:
        return None

def load_global_config(config_path: str) -> dict:
    with open(config_path, 'r') as file:
        # Assuming that config file is "small"
        return json.load(file)

def get_proxy_string(object_name: str, port = 10000) -> str:
    return f"{object_name}:default -p {port}"

def find_object_for_name(name: str, global_config: dict) -> tuple[dict, int] | None:
    server_list = global_config['servers']
    
    if server_list is None:
        print("Error while acquiring server list from global configuration")
        return None
    
    for server_config in server_list: 
        server_name, object_list, port = server_config['name'], server_config['objects'], server_config['port']

        if server_name is None or object_list is None:
            print("Invalid server configuration; no name or no objects provided")
            raise RuntimeError("Invalid server configuration; no name or no object provided")
            
        for object in object_list:
            if object['name'] == name:
                print(f"Object for name {name} was found: {object}")
                return object, port
        

    print(f"Failed to find object for name: {name}")
    return None

def get_device_list(communicator, ports: list[int] = [10000, 10001]) -> list[dict]:
    result = []
    for port in ports:
        base = communicator.stringToProxy(get_proxy_string("Controller", port))
        try:
            controller = Smarthome.Controller.ISmartHomeControllerPrx.checkedCast(base)
        except Exception as e:
            # ignore hehe or not really hehe
            continue

        if not controller:
            raise RuntimeError("Failed to acquire proxy for Controller")

        try:
            device_list = controller.getDevices()
            result.append(device_list)
        except Exception:
            # print("Request failed for port {port}")
            # ignore
            continue

    return result


def main():
    with Ice.initialize(sys.argv) as communicator:
        config_dir = os.environ.get(CONST_CONFIG_DIR)

        if config_dir is None:
            print(f"Failed to resolve {CONST_CONFIG_DIR} env variable")            

        config_path = config_dir + "/config.json"
        
        print("Config path:", config_path)

        global_config = load_global_config(config_path)

        print(global_config)

        base = communicator.stringToProxy("AirConditioner:default -p 10000")
        air_conditioner = Smarthome.AirConditioning.IAirConditionerPrx.checkedCast(base)

        if not air_conditioner:
            raise RuntimeError("Invalid proxy")

        running = True
        while running is True:
            user_input = str(input("(object or command)>> ")).strip()

            if user_input == "exit":
                running = False
                break

            if user_input == "show":
                print(get_device_list(communicator, [10000, 10001]))
                continue

            name = user_input

            obj, port = find_object_for_name(name, global_config)

            if obj is None:
                continue

            print(f"Select one of available methods:")
            print(map_obj_type_to_calls[obj['type']] + map_obj_type_to_calls['device'])

            method = str(input("(method name)>> "))
            
            result = handle_method_call(communicator, obj, method, port)

            if result is not None:
                print(result)


if __name__ == "__main__":
    main()

