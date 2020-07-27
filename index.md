# PiCar-Client
==============================

### THIS IS IN EARLY DEVELOPMENT

This is a simple android app that allows you to connect to a
Raspberry Pi running [piCar](https://github.com/jerome1232/PiCar),
view it's Mjpeg stream from a piCar, and control it using a virtual joystick.

##### Software required:

These will need to be installed on the Pi.

- [PiCar](https://jerome1232.github.io/PiCar/): Allows control of a PiCar over the network.
- [Mjpeg Streamer](https://github.com/jacksonliam/mjpg-streamer): Allows pi car to stream mjpeg of camera over network.
  
I'd recommend setting the Pi up as it's own access point that you connect to via WiFi
so that it's not limited to staying near another access point. Note after doing this, if you want
the Pi to have an internet connection you will need to connect it with ethernet.

To config Pi as it's own access point:

Adapted from: [thepie.io](https://thepi.io/how-to-use-your-raspberry-pi-as-a-wireless-access-point/)

```sh
# Update repositories, ensure pi OS is up to date
sudo apt update && sudo apt upgrade -y
# Install hostapd and dnsmasq
sudo apt install hostapd dnsmasq
# Stop running services
sudo systemctl stop hostapd
sudo systemctl stop dnsmasq
# Edit the config file, you can use any editor of your choice, nano is here for ease of use.
# Edit dhcpd config file to give the Pi a static ip address.
sudo nano /etc/dhcpcd.conf
```

An editor will open up and inside you should put the following at the end. If you are using a usb wifi stick
, the interface may be different. This configures the Pi's ip address.

```sh
interface wlan0
static ip_address=192.168.0.10/24
```

Save that with ctrl+o, exit nano with ctrl+x

Once that is done, edit the dnsmasq configuration file. This will allow the Pi to act as a
dhcp server handing out IP addresses to clients that connect to it.

```sh
# First just move the original config file, so that it's recoverable if wanted
sudo mv /etc/dnsmasq.conf /etc/dnsmasq.conf.orig
# Then edit a new config file with nano
sudo nano /etc/dnsmasq.conf
```

Again, a nano editor window will appear and you need to enter the line below.

```sh
interface=wlan0
    dhcp-range=192.168.0.11,192.168.0.30,255.255.255.0,24h
```

Now the last step is to configure the Pi's wifi to act as an access point. You should change the
password to your own unique password.

```sh
sudo nano /etc/hostapd/hostapd.conf
```

Add the lines below, this will need adjustments if you aren't using the built in WiFi

```sh
interface=wlan0
driver=nl80211
ssid=PiCar
hw_mode=g
channel=6
ieee80211n=1
wmm_enabled=1
ht_capab=[HT40][SHORT-GI-20][DSSS_CCK-40]
macaddr_acl=0
auth_algs=1
ignore_broacast_ssid=0
wpa=2
wpa_key_mgmt=WPA-PSK
wpa_passphrase=vroom
rsn_pairwise=CCMP
```

The last step would be to tell hostapd where the config file is.

```sh
sudo nano /etc/default/hostapd
```

Find the line below and delete the '#' in front of it.

```sh
# DAEMON_CONF="/etc/hostapd/hostapd.conf"
```

Becomes:

```sh
DAEMON_CONF="/etc/hostapd/hostapd.conf"
```

Save, exit, reboot the pi

```sh
sudo reboot now
```

Once the Pi has rebooted, you should be able to see it listed as an access point if you search
from any WiFi enabled device. Try connecting to it, it's new ip address once connected if you followed
this guide exactly will be '192.168.0.10'. You can try to ssh to it now.

#### TODO:
- [x] ~~Show MJPEG stream from pi.~~
- [ ] Reattempt connection when data stream stops
- [ ] Add ability to connect to tcp socket.
- [ ] Allow user config of port.
- [ ] Allow user config of pi IP address.
- [ ] Allow user config of pi SSID.
- [ ] Add virtual joystick.
- [ ] Send virtual joystick data over TCP socket to pi.
