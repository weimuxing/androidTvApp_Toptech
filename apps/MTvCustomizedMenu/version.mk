#<MStar Software>
#******************************************************************************
# MStar Software
# Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
# All software, firmware and related documentation herein ("MStar Software") are
# intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
# law, including, but not limited to, copyright law and international treaties.
# Any use, modification, reproduction, retransmission, or republication of all
# or part of MStar Software is expressly prohibited, unless prior written
# permission has been granted by MStar.
#
# By accessing, browsing and/or using MStar Software, you acknowledge that you
# have read, understood, and agree, to be bound by below terms ("Terms") and to
# comply with all applicable laws and regulations:
#
# 1. MStar shall retain any and all right, ownership and interest to MStar
#    Software and any modification/derivatives thereof.
#    No right, ownership, or interest to MStar Software and any
#    modification/derivatives thereof is transferred to you under Terms.
#
# 2. You understand that MStar Software might include, incorporate or be
#    supplied together with third party's software and the use of MStar
#    Software may require additional licenses from third parties.
#    Therefore, you hereby agree it is your sole responsibility to separately
#    obtain any and all third party right and license necessary for your use of
#    such third party's software.
#
# 3. MStar Software and any modification/derivatives thereof shall be deemed as
#    MStar's confidential information and you agree to keep MStar's
#    confidential information in strictest confidence and not disclose to any
#    third party.
#
# 4. MStar Software is provided on an "AS IS" basis without warranties of any
#    kind. Any warranties are hereby expressly disclaimed by MStar, including
#    without limitation, any warranties of merchantability, non-infringement of
#    intellectual property rights, fitness for a particular purpose, error free
#    and in conformity with any international standard.  You agree to waive any
#    claim against MStar for any loss, damage, cost or expense that you may
#    incur related to your use of MStar Software.
#    In no event shall MStar be liable for any direct, indirect, incidental or
#    consequential damages, including without limitation, lost of profit or
#    revenues, lost or damage of data, and unauthorized system use.
#    You agree that this Section 4 shall still apply without being affected
#    even if MStar Software has been modified by MStar in accordance with your
#    request or instruction for your use, except otherwise agreed by both
#    parties in writing.
#
# 5. If requested, MStar may from time to time provide technical supports or
#    services in relation with MStar Software to you for your use of
#    MStar Software in conjunction with your or your customer's product
#    ("Services").
#    You understand and agree that, except otherwise agreed by both parties in
#    writing, Services are provided on an "AS IS" basis and the warranty
#    disclaimer set forth in Section 4 above shall apply.
#
# 6. Nothing contained herein shall be construed as by implication, estoppels
#    or otherwise:
#    (a) conferring any license or right to use MStar name, trademark, service
#        mark, symbol or any other identification;
#    (b) obligating MStar or any of its affiliates to furnish any person,
#        including without limitation, you and your customers, any assistance
#        of any kind whatsoever, or any information; or
#    (c) conferring any license or right under any intellectual property right.
#
# 7. These terms shall be governed by and construed in accordance with the laws
#    of Taiwan, R.O.C., excluding its conflict of law rules.
#    Any and all dispute arising out hereof or related hereto shall be finally
#    settled by arbitration referred to the Chinese Arbitration Association,
#    Taipei in accordance with the ROC Arbitration Law and the Arbitration
#    Rules of the Association by three (3) arbitrators appointed in accordance
#    with the said Rules.
#    The place of arbitration shall be in Taipei, Taiwan and the language shall
#    be English.
#    The arbitration award shall be final and binding to both parties.
#
#******************************************************************************
#<MStar Software>


base_version_major := 1
# Change this for each branch
base_version_minor := 01
# The date of the first commit checked in to the current branch
base_version_since := 2015-09-15

# code_version_major will overflow at 22
code_version_major := $(shell echo $$(($(base_version_major)+3)))

git_commit_count := $(shell git --git-dir $(LOCAL_PATH)/.git rev-list --since=$(base_version_since) --no-merges --count HEAD)
code_version_build := $(shell printf "%03d" $$(($(git_commit_count))))

#####################################################
#####################################################
# Collect automatic version code parameters
ifneq "" "$(filter eng.%,$(BUILD_NUMBER))"
    # This is an eng build
    base_version_buildtype := 0
else
    # This is a build server build
    base_version_buildtype := 1
endif

ifeq "$(TARGET_ARCH)" "x86"
    base_version_arch := 7
else ifeq "$(TARGET_ARCH)" "mips"
    base_version_arch := 5
else ifeq "$(TARGET_ARCH)" "arm"
    ifeq ($(TARGET_ARCH_VARIANT),armv5te)
        base_version_arch := 1
    else
        base_version_arch := 3
    endif
else
    base_version_arch := 0
endif

# Currently supported densities.
base_version_density := 0

# Build the version code
version_code_package := $(code_version_major)$(base_version_minor)$(code_version_build)$(base_version_buildtype)$(base_version_arch)$(base_version_density)

# The version name scheme for the package apk is:
# - For eng build (t=0):     M.mm.bbb eng.$(USER)-hh-date-ad
# - For build server (t=1):  M.mm.bbb (nnnnnn-ad)
#       where nnnnnn is the build number from the build server (no zero-padding)
#       and hh is the git hash
# On eng builds, the BUILD_NUMBER has the user and timestamp inline
ifneq "" "$(filter eng.%,$(BUILD_NUMBER))"
    git_hash := $(shell git --git-dir $(LOCAL_PATH)/.git log -n 1 --pretty=format:%h)
    date_string := $(shell date +%m%d%y_%H%M%S)
    version_name_package := $(base_version_major).$(base_version_minor).$(code_version_build) (eng.$(USER).$(git_hash).$(date_string)-$(base_version_arch)$(base_version_density))
else
    version_name_package := $(base_version_major).$(base_version_minor).$(code_version_build) ($(BUILD_NUMBER)-$(base_version_arch)$(base_version_density))
endif

# Cleanup the locals
code_version_major :=
code_version_build :=
base_version_major :=
base_version_minor :=
base_version_since :=
base_version_buildtype :=
base_version_arch :=
base_version_density :=
git_hash :=
date_string :=
