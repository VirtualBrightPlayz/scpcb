Type Player
	;entities
	Field collider%

	Field head%
	Field headPitch#
	Field headYaw#

	Field cam%
	Field camShake#
	Field camAnimState#
	Field camZoom#

	Field camFogNear#
	Field camFogFar#

	Field overlays%[OVERLAY_COUNT]

	Field grabbedEntity%

	Field closestItem.Item
	Field selectedItem.Item

	Field closestButton%
	Field closestDoor.Door
	Field selectedDoor.Door
	;------------

	;movement states
	Field crouching%
	Field crouchState#

	Field moveSpeed#
	Field dropSpeed#

	Field blinkFreq#
	Field blinkTimer#
	Field stamina#

	Field footstepOverride%
	;------------

	;ailments
	Field dead%
	Field fallTimer#

	Field injuries#
	Field bloodloss#

	Field heartbeatIntensity#

	Field superMan#

	Field infect008#

	Field sanity895#

	Field forceMove#
	Field forceAngle#

	Field disableControls%

	Field blinkEffect#
	Field blinkEffectTimer#
	Field staminaEffect#
	Field staminaEffectTimer#

	Field lightFlash#
	Field blurTimer#

	Field loudness#
	;------

	;items
	Field inventory.Inventory
	Field openInventory.Inventory
	;------

	;sounds
	Field breathingSFX.IntArray
	Field heartbeat%
	Field bloodDrip%[4]

	Field breathChn%
	;------

	;other states
	Field currRoom.Room

	Field godMode%
	Field noclip%
	;------
End Type