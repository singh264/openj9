# Copyright IBM Corp. and others 2019
#
# This program and the accompanying materials are made available under
# the terms of the Eclipse Public License 2.0 which accompanies this
# distribution and is available at https://www.eclipse.org/legal/epl-2.0/
# or the Apache License, Version 2.0 which accompanies this distribution and
# is available at https://www.apache.org/licenses/LICENSE-2.0.
#
# This Source Code may also be made available under the following
# Secondary Licenses when the conditions for such availability set
# forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
# General Public License, version 2 with the GNU Classpath
# Exception [1] and GNU General Public License, version 2 with the
# OpenJDK Assembly Exception [2].
#
# [1] https://www.gnu.org/software/classpath/license.html
# [2] https://openjdk.org/legal/assembly-exception.html
#
# SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception

# To use this docker file:
#   docker build -t=openj9aarch64 .
#   docker run -it openj9aarch64

FROM ubuntu:16.04

# Define default user name
ARG user=jenkins

# Install required OS tools
RUN apt-get update \
  && apt-get install -qq -y --no-install-recommends \
    software-properties-common \
    python-software-properties \
  && add-apt-repository ppa:ubuntu-toolchain-r/test \
  && apt-get update \
  && apt-get install -qq -y --no-install-recommends \
    autoconf \
    ca-certificates \
    ccache \
    cmake \
    cpio \
    file \
    g++-7 \
    gcc-7 \
    git \
    git-core \
    make \
    pkg-config \
    qemu \
    realpath \
    ssh \
    unzip \
    vim-tiny \
    wget \
    zip \
  && rm -rf /var/lib/apt/lists/*

# Add user home
RUN useradd -ms /bin/bash --uid 1000 --home-dir /home/$user $user

# Create links for c++,g++,cc,gcc
RUN ln -s g++ /usr/bin/c++ \
  && ln -s g++-7 /usr/bin/g++ \
  && ln -s gcc /usr/bin/cc \
  && ln -s gcc-7 /usr/bin/gcc

# Download and setup freemarker.jar
RUN cd /home/$user \
  && wget https://sourceforge.net/projects/freemarker/files/freemarker/2.3.8/freemarker-2.3.8.tar.gz/download -O freemarker.tgz \
  && tar -xzf freemarker.tgz freemarker-2.3.8/lib/freemarker.jar --strip=2 \
  && rm -f freemarker.tgz

# Download and install boot JDK from AdoptOpenJDK
RUN cd /home/$user \
  && wget -O bootjdk11.tar.gz "https://api.adoptopenjdk.net/v2/binary/releases/openjdk11?openjdk_impl=openj9&os=linux&arch=x64&release=latest&heap_size=normal&type=jdk" \
  && tar -xzf bootjdk11.tar.gz \
  && rm -f bootjdk11.tar.gz \
  && mv $(ls | grep -i jdk) bootjdk11

# get the toolchain
RUN cd /home/$user \
  && wget https://releases.linaro.org/components/toolchain/binaries/7.5-2019.12/aarch64-linux-gnu/gcc-linaro-7.5.0-2019.12-x86_64_aarch64-linux-gnu.tar.xz \
  && tar xf gcc-linaro-7.5.0-2019.12-x86_64_aarch64-linux-gnu.tar.xz \
  && rm gcc-linaro-7.5.0-2019.12-x86_64_aarch64-linux-gnu.tar.xz

# Get the debs for native libraries on the target platform
# These were current releases in stretch as of Nov 2017
# We might need older libraries for older OS releases, and presumably at
# some point we might need to update to newer ones.
# Libraries can be found through https://www.debian.org/distrib/packages
# TODO Mirror site should be configurable.
ADD \
    http://ftp.us.debian.org/debian/pool/main/a/alsa-lib/libasound2_1.1.3-5_arm64.deb                \
    http://ftp.us.debian.org/debian/pool/main/a/alsa-lib/libasound2-dev_1.1.3-5_arm64.deb            \
    http://ftp.us.debian.org/debian/pool/main/c/cups/libcups2-dev_2.2.1-8+deb9u6_arm64.deb           \
    http://ftp.us.debian.org/debian/pool/main/c/cups/libcupsimage2-dev_2.2.1-8+deb9u6_arm64.deb      \
    http://ftp.us.debian.org/debian/pool/main/d/dwarfutils/libdwarf-dev_20180809-1_arm64.deb         \
    http://ftp.us.debian.org/debian/pool/main/e/elfutils/libelf-dev_0.176-1.1_arm64.deb              \
    http://ftp.us.debian.org/debian/pool/main/f/freetype/libfreetype6_2.6.3-3.2+deb9u1_arm64.deb     \
    http://ftp.us.debian.org/debian/pool/main/f/freetype/libfreetype6-dev_2.6.3-3.2+deb9u1_arm64.deb \
    http://ftp.us.debian.org/debian/pool/main/f/fontconfig/libfontconfig1_2.13.1-2_arm64.deb         \
    http://ftp.us.debian.org/debian/pool/main/f/fontconfig/libfontconfig1-dev_2.13.1-2_arm64.deb     \
    http://ftp.us.debian.org/debian/pool/main/libi/libice/libice-dev_1.0.9-2_arm64.deb               \
    http://ftp.us.debian.org/debian/pool/main/libp/libpng1.6/libpng16-16_1.6.28-1+deb9u1_arm64.deb   \
    http://ftp.us.debian.org/debian/pool/main/libp/libpng1.6/libpng-dev_1.6.28-1+deb9u1_arm64.deb    \
    http://ftp.us.debian.org/debian/pool/main/libs/libsm/libsm-dev_1.2.2-1+b3_arm64.deb              \
    http://ftp.us.debian.org/debian/pool/main/o/openssl/libssl1.1_1.1.1k-1_arm64.deb                 \
    http://ftp.us.debian.org/debian/pool/main/o/openssl/libssl-dev_1.1.1k-1_arm64.deb                \
    http://ftp.us.debian.org/debian/pool/main/libx/libx11/libx11-6_1.6.4-3%2bdeb9u1_arm64.deb        \
    http://ftp.us.debian.org/debian/pool/main/libx/libx11/libx11-dev_1.6.4-3%2bdeb9u1_arm64.deb      \
    http://ftp.us.debian.org/debian/pool/main/libx/libxext/libxext6_1.3.3-1+b2_arm64.deb             \
    http://ftp.us.debian.org/debian/pool/main/libx/libxext/libxext-dev_1.3.3-1+b2_arm64.deb          \
    http://ftp.us.debian.org/debian/pool/main/libx/libxi/libxi6_1.7.9-1_arm64.deb                    \
    http://ftp.us.debian.org/debian/pool/main/libx/libxi/libxi-dev_1.7.9-1_arm64.deb                 \
    http://ftp.us.debian.org/debian/pool/main/libx/libxrandr/libxrandr-dev_1.5.1-1_arm64.deb         \
    http://ftp.us.debian.org/debian/pool/main/libx/libxrandr/libxrandr2_1.5.1-1_arm64.deb            \
    http://ftp.us.debian.org/debian/pool/main/libx/libxrender/libxrender1_0.9.10-1_arm64.deb         \
    http://ftp.us.debian.org/debian/pool/main/libx/libxrender/libxrender-dev_0.9.10-1_arm64.deb      \
    http://ftp.us.debian.org/debian/pool/main/libx/libxt/libxt-dev_1.1.5-1_arm64.deb                 \
    http://ftp.us.debian.org/debian/pool/main/libx/libxtst/libxtst6_1.2.3-1_arm64.deb                \
    http://ftp.us.debian.org/debian/pool/main/libx/libxtst/libxtst-dev_1.2.3-1_arm64.deb             \
    http://ftp.us.debian.org/debian/pool/main/x/x11proto-core/x11proto-core-dev_7.0.31-1_all.deb     \
    http://ftp.us.debian.org/debian/pool/main/x/x11proto-input/x11proto-input-dev_2.3.2-1_all.deb    \
    http://ftp.us.debian.org/debian/pool/main/x/x11proto-kb/x11proto-kb-dev_1.0.7-1_all.deb          \
    http://ftp.us.debian.org/debian/pool/main/x/x11proto-randr/x11proto-randr-dev_1.5.0-1_all.deb    \
    http://ftp.us.debian.org/debian/pool/main/x/x11proto-render/x11proto-render-dev_0.11.1-2_all.deb \
    http://ftp.us.debian.org/debian/pool/main/x/x11proto-xext/x11proto-xext-dev_7.3.0-1_all.deb      \
    http://ftp.us.debian.org/debian/pool/main/z/zlib/zlib1g_1.2.8.dfsg-5_arm64.deb                   \
    /home/$user/debs/

# unpack debs into toolchain libc dir
RUN cd /home/$user/debs \
  && for f in *.deb; do dpkg-deb -x $f .; done \
  && cp -r usr/include/* ../gcc-linaro-7.5.0-2019.12-x86_64_aarch64-linux-gnu/aarch64-linux-gnu/libc/usr/include/ \
  && cp -r usr/lib/aarch64-linux-gnu/* ../gcc-linaro-7.5.0-2019.12-x86_64_aarch64-linux-gnu/aarch64-linux-gnu/libc/usr/lib/ \
  && cp lib/aarch64-linux-gnu/* ../gcc-linaro-7.5.0-2019.12-x86_64_aarch64-linux-gnu/aarch64-linux-gnu/libc/lib/ \
  && rm -rf /home/$user/debs

# Env vars set here will be visible in the running container, so can be
# used to convey configuration information to the build scripts.

# Set environment variable JAVA_HOME, and prepend $JAVA_HOME/bin to PATH
ENV JAVA_HOME=/home/$user/bootjdk11
ENV PATH="$JAVA_HOME/bin:$PATH"

# Directory containing the cross compilation tool chain
ENV OPENJ9_CC_DIR=/home/$user/gcc-linaro-7.5.0-2019.12-x86_64_aarch64-linux-gnu

# Prefix for the cross compilation tools (without the trailing '-')
ENV OPENJ9_CC_PREFIX=aarch64-linux-gnu

# Add the toolchain bin dir to the PATH for convenience
ENV PATH="$PATH:$OPENJ9_CC_DIR/bin"

RUN chown -R $user:$user /home/$user
USER $user
WORKDIR /home/$user
