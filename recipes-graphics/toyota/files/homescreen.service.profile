[Unit]
Requires=weston.service
After=weston.service

[Service]
# Placeholder configuration to run as weston user for now.
User=weston
Group=weston
Environment=XDG_RUNTIME_DIR=/run/user/200
ExecStart=/usr/bin/homescreen --f --observatory-host 0.0.0.0 --observatory-port 1234 --start-paused

[Install]
WantedBy=graphical.target
