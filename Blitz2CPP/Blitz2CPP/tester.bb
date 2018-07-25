Global camera% = CreateCamera(), light%=CreateLight()
Global cube%=CreateCube()

; Load texture
Global tex% = LoadTexture("media/b3dlogo.jpg")

Function ligger%(for1#, plyr.Player, for2#=3)
    ; -------------------
    ; LoadTexture Example
    ; -------------------

    Graphics3D(640,480)
    SetBuffer(BackBuffer())

    RotateEntity(light,90,0,0)
    PositionEntity(cube,0,0,5)

    ; Texture cube with texture
    EntityTexture(cube,tex)

	; While loop.

    While (Not KeyDown(1)) ;This comment was originally inline right next to the while loop. It should be above now.
        Local pitch#=0, yaw#=0
        Local roll#=0, test$
        test ="this is a test string to make sure that boolean operators aren't parsed inside literals. Not Or And Xor ; "

        If (KeyDown(208)) Then pitch=-1
        If (KeyDown(200)) Then pitch=1
        If (KeyDown(203)) Then yaw=-1
        If (KeyDown(205)) Then yaw=1
        If (KeyDown(45)) Then roll=-1
        If (KeyDown(44)) Then roll=1

        ;Test comment.
        TurnEntity(cube, pitch, yaw, roll)

        RenderWorld()
        Flip()
    Wend

	; If statement testing.
	If (someBoolean Or someOtherBoolean) Then
		If ((moreBooleans)And(uhuh)) Then
			; nested ifs work!
		EndIf
	ElseIf (test <> True) Then
		If (uh = False) Then
			;test
		Else
			;more test
		EndIf
	Else
		;test everything
	EndIf

	; For loops.
	Local i%
    For i=2 To 32
        Local test$="Test For Loop" ;this comment was next to the line below
    Next

    For i=0 To 5 Step MyJuss()
        Local test$="Test For Loop with Step"
    Next

	; For Each loops.
	For plyr = Each Player
		; Test accessing data members.
		plyr\loudness = 2.0

		plyr = New Player
		; Nested For Each with unique iterator name.
		For plyr = Each Player
		Next
	Next
	Delete plyr

	; Switches.
	Select (i)
		Case 5
			If (something) Then
				;Testing nested scopes.
			EndIf
		Case 32
			;More scope checking.
		Default
			;Default scope
	End Select

	Return i
End Function

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