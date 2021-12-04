SUMMARY = "Toyota IVI Homescreen"
DESCRIPTION = "Toyota's Flutter Embedder that communicates with AGL-compositor/Wayland compositors"
AUTHOR = "joel.winarske@toyotaconnected.com"
HOMEPAGE = "https://github.com/toyota-connected/ivi-homescreen"
BUGTRACKER = "https://github.com/toyota-connected/ivi-homescreen/issues"
SECTION = "graphics"
CVE_PRODUCT = "homescreen"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=39ae29158ce710399736340c60147314"

DEPENDS += "\
    compiler-rt \
    flutter-engine \
    libcxx \
    virtual/egl \
    wayland \
    wayland-native \
    wayland-protocols \
    "

REQUIRED_DISTRO_FEATURES = "wayland opengl"

SRCREV ??= "${AUTOREV}"
SRC_URI = "git://github.com/toyota-connected/ivi-homescreen.git;protocol=https;branch=main \
           file://homescreen.service.debug \
           file://homescreen.service.profile \
           file://homescreen.service.release \
          "

S = "${WORKDIR}/git"

inherit cmake features_check systemd

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

EXTRA_OECMAKE += "\
    -D CMAKE_SYSROOT=${STAGING_DIR_TARGET}/usr \
    "

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'systemd flutter-release flutter-profile flutter-debug', d)}"

do_install:append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}
        if ${@bb.utils.contains('PACKAGECONFIG', 'flutter-release', 'true', 'false', d)}; then
            install -m 644 ${WORKDIR}/homescreen.service.release ${D}${systemd_system_unitdir}
        elif ${@bb.utils.contains('PACKAGECONFIG', 'flutter-profile', 'true', 'false', d)}; then
            install -m 644 ${WORKDIR}/homescreen.service.profile ${D}${systemd_system_unitdir}
        elif ${@bb.utils.contains('PACKAGECONFIG', 'flutter-debug', 'true', 'false', d)}; then
            install -m 644 ${WORKDIR}/homescreen.service.debug ${D}${systemd_system_unitdir}
        fi
    fi
}

SYSTEMD_SERVICE:${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'homescreen.service', '', d)}"
SYSTEMD_PACKAGES = "${@bb.utils.contains('PACKAGECONFIG', 'systemd', '${PN}', '', d)}"
