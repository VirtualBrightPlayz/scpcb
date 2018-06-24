Function FillRoom_test_860_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    ;the wooden door
    r\objects[2] = LoadMesh("GFX/Map/forest/door_frame.b3d")
    PositionEntity(r\objects[2],r\x + 184.0 * RoomScale,0,r\z,True)
    ScaleEntity(r\objects[2],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True)
    EntityParent(r\objects[2],r\obj)

    r\objects[3] =  LoadMesh("GFX/Map/forest/door.b3d")
    PositionEntity(r\objects[3],r\x + 112.0 * RoomScale,0,r\z+0.05,True)
    EntityType(r\objects[3], HIT_MAP)

    ScaleEntity(r\objects[3],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True)
    EntityParent(r\objects[3],r\obj)

    r\objects[4] = CopyEntity(r\objects[3])
    PositionEntity(r\objects[4],r\x + 256.0 * RoomScale,0,r\z-0.05,True)
    RotateEntity(r\objects[4], 0,180,0)
    ScaleEntity(r\objects[4],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True)
    EntityParent(r\objects[4],r\obj)

    ;doors to observation booth
    d = CreateDoor(r\zone, r\x + 928.0 * RoomScale,0,r\z + 640.0 * RoomScale,0,r,False,False,False,"ABCD")
    d = CreateDoor(r\zone, r\x + 928.0 * RoomScale,0,r\z - 640.0 * RoomScale,0,r,True,False,False,"ABCD")
    d\autoClose = False

    ;doors to the room itself
    d = CreateDoor(r\zone, r\x+416.0*RoomScale,0,r\z - 640.0 * RoomScale,0,r,False,False,1)
    d = CreateDoor(r\zone, r\x+416.0*RoomScale,0,r\z + 640.0 * RoomScale,0,r,False,False,1)

    ;the forest
    Local fr.Forest = New Forest
    r\fr=fr
    GenForestGrid(fr)
    PlaceForest(fr,r\x,r\y+30.0,r\z,r)
    ;EntityParent(fr\forest_Pivot,r\obj)

    it = CreatePaper("doc860", r\x + 672.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 335.0 * RoomScale)
    RotateEntity(it\collider, 0, r\angle+10, 0)
    EntityParent(it\collider, r\obj)

    it = CreatePaper("doc8601", r\x + 1152.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 384.0 * RoomScale)
    RotateEntity(it\collider, 0, r\angle+170, 0)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_test_860_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#, ang#

	;[Block]

	Local fr.Forest=e\room\fr

	If (mainPlayer\currRoom = e\room And fr<>Null) Then

		;Local dp.DrawPortal

		If (e\eventState=1.0) Then ;the player is in the forest
			mainPlayer\footstepOverride = 2
			e\overwriteMusic = True

			Curr106\idle = True

			;ShowEntity(fr\detailEntities[0])
			;ShowEntity(fr\detailEntities[1])

			UpdateForest(fr,mainPlayer\collider)

			If (Not e\loaded) Then
				e\musicTrack = MUS_8601
				;If (Music(12) = 0) Then Music(12) = LoadSound("SFX/Music/8601Cancer.ogg") ;TODO: fix
				If (e\room\npc[0]=Null) Then
					e\room\npc[0]=CreateNPC(NPCtype860, 0,0,0)
				EndIf

				e\loaded = True
			EndIf

			If (e\room\npc[0]<>Null) Then
				If (e\room\npc[0]\state2 = 1 And e\room\npc[0]\state>1) Then ;the monster is chasing the player
					e\overwriteMusic = False
					SetNextMusicTrack(MUS_8602, False)
				Else
					e\overwriteMusic = True
				EndIf
			EndIf

			;the player fell
			If (EntityY(mainPlayer\collider)<=28.5) Then
				Kill(mainPlayer)
				mainPlayer\blinkTimer=-2
			ElseIf (EntityY(mainPlayer\collider)>EntityY(fr\forest_Pivot,True)+0.5) Then
				MoveEntity(mainPlayer\collider, 0, ((EntityY(fr\forest_Pivot,True)+0.5) - EntityY(mainPlayer\collider))*timing\tickDuration, 0)
			EndIf

			If (e\room\npc[0]<>Null) Then
				If (e\room\npc[0]\state = 0 Or EntityDistance(mainPlayer\collider, e\room\npc[0]\collider)>12.0) Then
					e\eventState3 = e\eventState3 + (1+mainPlayer\moveSpeed)* timing\tickDuration
					If ((e\eventState3 Mod 500) < 10.0 And ((e\eventState3-timing\tickDuration) Mod 500) > 490.0) Then
						;If (e\eventState3 > 3500 And Rnd(10000)<e\eventState3) Then
						If (e\eventState3 > 3000-(500*SelectedDifficulty\aggressiveNPCs) And Rnd(10000+(500*SelectedDifficulty\aggressiveNPCs)) < e\eventState3) Then
							e\room\npc[0]\state=2
							PositionEntity(e\room\npc[0]\collider, 0,-110,0)
							;e\eventState3=e\eventState3-Rnd(2000,3000)
							e\eventState3=e\eventState3-Rnd(1000,2000-(500*SelectedDifficulty\aggressiveNPCs))
							DebugLog("attack")
						Else
							e\room\npc[0]\state=1
							PositionEntity(e\room\npc[0]\collider, 0,-110,0)
							DebugLog("spawn")
						EndIf
					EndIf
				EndIf
			EndIf

			;If (KeyHit(25)) Then
			;	e\room\npc[0]\state=2
			;	PositionEntity(e\room\npc[0]\collider, 0,-110,0)
			;	e\eventState3=e\eventState3-Rnd(2000,3000)
			;EndIf

			For i = 0 To 1
				If (EntityDistance(fr\door[i], mainPlayer\collider)<0.5) Then
					If (EntityInView(fr\door[i], mainPlayer\cam)) Then
						DrawHandIcon = True
						If (MouseHit1) Then
							If (i=1) Then
								mainPlayer\blinkTimer = -10

								PlaySound2(LoadTempSound("SFX/Door/WoodenDoorOpen.ogg"))

								RotateEntity(e\room\objects[3], 0, 0, 0)
								RotateEntity(e\room\objects[4], 0, 180, 0)
								;SetAnimTime(e\room\objects[3], 0.0)
								;SetAnimTime(e\room\objects[4], 0.0)

								;dp.DrawPortal=e\room\dp;Object.DrawPortal(e\room\objects[0])
								PositionEntity(mainPlayer\collider, EntityX(e\room\objects[2],True),0.5,EntityZ(e\room\objects[2],True))

								RotateEntity(mainPlayer\collider, 0, EntityYaw(e\room\obj,True)+e\eventState2*180, 0)
								MoveEntity(mainPlayer\collider, 0,0,1.5)

								ResetEntity(mainPlayer\collider)

								UpdateDoorsTimer = 0
								UpdateDoors()

								e\eventState = 0.0

							Else
								PlaySound2(LoadTempSound("SFX/Door/WoodenDoorBudge.ogg"))
								Msg = "The door will not budge."
								MsgTimer = 70*5
							EndIf
						EndIf
					EndIf
				EndIf
			Next

			If (e\room\npc[0]<>Null) Then
				x = Max(1.0-(e\room\npc[0]\state3/300.0),0.1)
			Else
				x = 2.0
			EndIf

			CameraClsColor(mainPlayer\cam,98*x,133*x,162*x)
			CameraRange(mainPlayer\cam,RoomScale,8.5)
			CameraFogRange(mainPlayer\cam,0.5,8.0)
			CameraFogColor(mainPlayer\cam,98*x,133*x,162*x)

		Else

			If (Not Contained106) Then Curr106\idle = False

			;dp.DrawPortal=e\room\dp;Object.DrawPortal(e\room\objects[0])

			;HideEntity(fr\detailEntities[0])
			;HideEntity(fr\detailEntities[1])

			If (EntityYaw(e\room\objects[3])=0.0) Then
				HideEntity(fr.Forest\forest_Pivot)
				If (Abs(Distance(EntityX(e\room\objects[3],True),EntityZ(e\room\objects[3],True),EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True)))<1.0) Then
					DrawHandIcon = True

					If (mainPlayer\selectedItem = Null) Then
						If (MouseHit1) Then
							PlaySound2(LoadTempSound("SFX/Door/WoodenDoorBudge.ogg"))
							Msg = "The door will not budge."
							MsgTimer = 5*70
						EndIf
					ElseIf (mainPlayer\selectedItem\template\name="scp860") Then
						If (MouseHit1) Then
							PlaySound2(LoadTempSound("SFX/Door/WoodenDoorOpen.ogg"))
							ShowEntity(fr.Forest\forest_Pivot)
							mainPlayer\selectedItem = Null

							mainPlayer\blinkTimer = -10

							e\eventState=1.0

							PositionEntity(mainPlayer\collider,EntityX(fr\door[0],True),EntityY(fr\door[0],True)+EntityY(mainPlayer\collider,True)+0.5,EntityZ(fr\door[0],True),True)

							RotateEntity(mainPlayer\collider, 0.0, EntityYaw(fr\door[0],True)-180, 0.0, True)
							MoveEntity(mainPlayer\collider, -0.5,0.0,0.5)



							pvt = CreatePivot()
							PositionEntity(pvt, EntityX(mainPlayer\cam),EntityY(mainPlayer\cam),EntityZ(mainPlayer\cam))
							PointEntity(pvt, e\room\obj)
							ang# = WrapAngle(EntityYaw(pvt)-EntityYaw(e\room\obj,True))
							If (ang > 90 And ang < 270) Then
								;TurnEntity(mainPlayer\collider,0,180+90,0,True)
								e\eventState2 = 1
							Else
									TurnEntity(mainPlayer\collider,0,90,0,True)
							;RotateEntity(mainPlayer\collider,0,EntityYaw(fr\door[0],True)+EntityYaw(mainPlayer\collider)-EntityYaw(dp\portal,True),0,True)
								e\eventState2 = 0
							EndIf
							FreeEntity(pvt)

							ResetEntity(mainPlayer\collider)



							;RotateEntity(e\room\objects[3], 0, 0.5, 0)
							;RotateEntity(e\room\objects[3], 0, 359.5, 0)
						EndIf
					EndIf
				EndIf
			EndIf

		EndIf

	Else
		If (fr=Null) Then
			RemoveEvent(e)
		Else
			If (fr\forest_Pivot<>0) Then HideEntity(fr\forest_Pivot)
		EndIf
	EndIf

	;[End Block]

End Function

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;Forest gen consts
Const gridsize% = 10
Const deviation_chance% = 40 ;out of 100
Const branch_chance% = 65
Const branch_max_life% = 4
Const branch_die_chance% = 18
Const max_deviation_distance% = 3
Const return_chance% = 27
Const center% = 5 ;(gridsize-1) / 2

Type Forest
	Field tileMesh%[6]
	Field detailMesh%[6]
	Field tileTexture%[10]
	Field grid%[(gridsize*gridsize)+11]
	Field tileEntities%[(gridsize*gridsize)+1]
	Field forest_Pivot%

	Field door%[2]
	Field detailEntities%[2]

	Field id%
End Type

Function move_forward%(dir%,pathx%,pathy%,retval%=0)
	;move 1 unit along the grid in the designated direction
	If (dir = 1) Then
		If (retval=0) Then
			Return pathx
		Else
			Return pathy+1
		EndIf
	EndIf
	If (retval=0) Then
		Return pathx-1+dir
	Else
		Return pathy
	EndIf
End Function

Function chance%(chanc%)
	;perform a chance given a probability
	Return (Rand(0,100)<=chanc)
End Function

Function turn_if_deviating%(max_deviation_distance_%,pathx%,center_%,dir%,retval%=0)
	;check if deviating and return the answer. if deviating, turn around
	Local current_deviation% = center_ - pathx
	Local deviated% = False
	If (dir = 0 And current_deviation >= max_deviation_distance_) Or (dir = 2 And current_deviation <= -max_deviation_distance_) Then
		dir = (dir + 2) Mod 4
		deviated = True
	EndIf
	If (retval=0) Then
		Return dir
	Else
		Return deviated
	EndIf
End Function

Global LastForestID% = 0

Function GenForestGrid(fr.Forest)
	fr\id=LastForestID+1
	LastForestID=LastForestID+1

	Local door1_pos%,door2_pos%
	Local i%,j%, n%
	door1_pos=Rand(3,7)
	door2_pos=Rand(3,7)

	;clear the grid
	For i=0 To gridsize-1
		For j=0 To gridsize-1
			fr\grid[(j*gridsize)+i]=0
		Next
	Next

	;set the position of the concrete and doors
	;For i=0 To gridsize-1
	;	fr\grid[i]=2
	;	fr\grid[((gridsize-1)*gridsize)+i]=2
	;Next
	fr\grid[door1_pos]=3
	fr\grid[((gridsize-1)*gridsize)+door2_pos]=3

	;generate the path
	Local pathx% = door2_pos
	Local pathy% = 1
	Local dir% = 1 ;0 = left, 1 = up, 2 = right
	fr\grid[((gridsize-1-pathy)*gridsize)+pathx] = 1

	Local deviated%

	While pathy < gridsize -4
		If (dir = 1) Then ;determine whether to go forward or to the side
			If (chance(deviation_chance)) Then
				;pick a branch direction
				dir = 2 * Rand(0,1)
				;make sure you have not passed max side distance
				dir = turn_if_deviating(max_deviation_distance,pathx,center,dir)
				deviated = turn_if_deviating(max_deviation_distance,pathx,center,dir,1)
				If (deviated) Then fr\grid[((gridsize-1-pathy)*gridsize)+pathx]=1
				pathx=move_forward(dir,pathx,pathy)
				pathy=move_forward(dir,pathx,pathy,1)
			EndIf

		Else
			;we are going to the side, so determine whether to keep going or go forward again
			dir = turn_if_deviating(max_deviation_distance,pathx,center,dir)
			deviated = turn_if_deviating(max_deviation_distance,pathx,center,dir,1)
			If (deviated Or chance(return_chance)) Then dir = 1

			pathx=move_forward(dir,pathx,pathy)
			pathy=move_forward(dir,pathx,pathy,1)
			;if we just started going forward go twice so as to avoid creating a potential 2x2 line
			If (dir=1) Then
				fr\grid[((gridsize-1-pathy)*gridsize)+pathx]=1
				pathx=move_forward(dir,pathx,pathy)
				pathy=move_forward(dir,pathx,pathy,1)
			EndIf
		EndIf

		;add our position to the grid
		fr\grid[((gridsize-1-pathy)*gridsize)+pathx]=1

	Wend
	;finally, bring the path back to the door now that we have reached the end
	dir = 1
	While pathy < gridsize-2
		pathx=move_forward(dir,pathx,pathy)
		pathy=move_forward(dir,pathx,pathy,1)
		fr\grid[((gridsize-1-pathy)*gridsize)+pathx]=1
	Wend

	If (pathx<>door1_pos) Then
		dir=0
		If (door1_pos>pathx) Then dir=2
		While pathx<>door1_pos
			pathx=move_forward(dir,pathx,pathy)
			pathy=move_forward(dir,pathx,pathy,1)
			fr\grid[((gridsize-1-pathy)*gridsize)+pathx]=1
		Wend
	EndIf

	;attempt to create new branches
	Local new_y%,temp_y%,new_x%
	Local branch_type%,branch_pos%
	Local leftmost%, rightmost%
	new_y=-3 ;used for counting off; branches will only be considered once every 4 units so as to avoid potentially too many branches
	While new_y<gridsize-6
		new_y=new_y+4
		temp_y=new_y
		new_x=0
		If (chance(branch_chance)) Then
			branch_type=-1
			;If (chance(cobble_chance)) Then
			;	branch_type=-2
			;EndIf
			;create a branch at this spot
			;determine if on left or on right
			branch_pos=2*Rand(0,1)
			;get leftmost or rightmost path in this row
			leftmost% = gridsize
			rightmost% = 0
			For i=0 To gridsize
				If (fr\grid[((gridsize-1-new_y)*gridsize)+i]=1) Then
					If (i<leftmost) Then leftmost=i
					If (i>rightmost) Then rightmost=i
				EndIf
			Next
			If (branch_pos=0) Then
				new_x=leftmost-1
			Else
				new_x=rightmost+1
			EndIf
			;before creating a branch make sure there are no 1's above or below
			If ((temp_y<>0 And fr\grid[((gridsize-1-temp_y+1)*gridsize)+new_x]=1) Or fr\grid[((gridsize-1-temp_y-1)*gridsize)+new_x]=1) Then
				Exit ;break simply to stop creating the branch
			EndIf
			fr\grid[((gridsize-1-temp_y)*gridsize)+new_x]=branch_type ;make 4s so you don't confuse your branch for a path; will be changed later
			If (branch_pos=0) Then
				new_x=leftmost-2
			Else
				new_x=rightmost+2
			EndIf
			fr\grid[((gridsize-1-temp_y)*gridsize)+new_x]=branch_type ;branch out twice to avoid creating an unwanted 2x2 path with the real path
			i = 2
			While i<branch_max_life
				i=i+1
				If (chance(branch_die_chance)) Then
					Exit
				EndIf
				If (Rand(0,3)=0) Then ;have a higher chance to go up to confuse the player
					If (branch_pos = 0) Then
						new_x=new_x-1
					Else
						new_x=new_x+1
					EndIf
				Else
					temp_y=temp_y+1
				EndIf

				;before creating a branch make sure there are no 1's above or below
				n% = ((gridsize - 1 - temp_y + 1)*gridsize)+new_x
				If (n < gridsize-1) Then
					If (temp_y <> 0 And fr\grid[n]=1) Then Exit
				EndIf
				n=((gridsize - 1 - temp_y - 1)*gridsize)+new_x
				If (n>0) Then
					If (fr\grid[n]=1) Then Exit
				EndIf

				;If ((temp_y <> 0 And fr\grid[((gridsize - 1 - temp_y + 1)*gridsize)+new_x]=1) Or fr\grid[((gridsize - 1 - temp_y - 1)*gridsize)+new_x] = 1) Then
				;	Exit
				;EndIf
				fr\grid[((gridsize-1-temp_y)*gridsize)+new_x]=branch_type ;make 4s so you don't confuse your branch for a path; will be changed later
				If (temp_y>=gridsize-2) Then Exit
			Wend
		EndIf
	Wend

	;change branches from 4s to 1s (they were 4s so that they didn't accidently create a 2x2 path unintentionally)
	For i=0 To gridsize-1
		For j=0 To gridsize-1
			If (fr\grid[(i*gridsize)+j]=-1) Then
				fr\grid[(i*gridsize)+j]=1
			ElseIf (fr\grid[(i*gridsize)+j]=-2) Then
				fr\grid[(i*gridsize)+j]=1
			;ElseIf fr\grid[(i*gridsize)+j]=0

			EndIf
		Next
	Next

End Function

Function PlaceForest(fr.Forest,x#,y#,z#,r.Rooms)
	;local variables
	Local tx%,ty%
	Local tile_size#=12.0
	Local tile_type%
	Local tile_entity%,detail_entity%

	Local tempf1#,tempf2#,tempf3#
	Local i%, angle%
	Local itemPlaced%[4]
	Local it.Items
	Local width%
	Local tempf4#
	Local lx%, ly%, d%, frame%

	If (fr\forest_Pivot<>0) Then
		FreeEntity(fr\forest_Pivot)
		fr\forest_Pivot=0
	EndIf
	For i%=0 To 3
		If (fr\tileMesh[i]<>0) Then
			FreeEntity(fr\tileMesh[i])
			fr\tileMesh[i]=0
		EndIf
	Next
	For i%=0 To 4
		If (fr\detailMesh[i]<>0) Then
			FreeEntity(fr\detailMesh[i])
			fr\detailMesh[i]=0
		EndIf
	Next
	For i%=0 To 9
		If (fr\tileTexture[i]<>0) Then
			FreeEntity(fr\tileTexture[i])
			fr\tileTexture[i]=0
		EndIf
	Next

	fr\forest_Pivot=CreatePivot()
	PositionEntity(fr\forest_Pivot,x,y,z,True)

	;load assets

	Local hmap%[ROOM4], mask%[ROOM4]
	Local GroundTexture% = LoadTexture("GFX/Map/forest/forestfloor.jpg")
	;TextureBlend(GroundTexture, FE_ALPHACURRENT)
	Local PathTexture% = LoadTexture("GFX/Map/forest/forestpath.jpg")
	;TextureBlend(PathTexture, FE_ALPHACURRENT)

	hmap[ROOM1]=LoadImage("GFX/Map/forest/forest1h.png")
	mask[ROOM1]=LoadTexture("GFX/Map/forest/forest1h_mask.png",1+2)

	hmap[ROOM2]=LoadImage("GFX/Map/forest/forest2h.png")
	mask[ROOM2]=LoadTexture("GFX/Map/forest/forest2h_mask.png",1+2)

	hmap[ROOM2C]=LoadImage("GFX/Map/forest/forest2Ch.png")
	mask[ROOM2C]=LoadTexture("GFX/Map/forest/forest2Ch_mask.png",1+2)

	hmap[ROOM3]=LoadImage("GFX/Map/forest/forest3h.png")
	mask[ROOM3]=LoadTexture("GFX/Map/forest/forest3h_mask.png",1+2)

	hmap[ROOM4]=LoadImage("GFX/Map/forest/forest4h.png")
	mask[ROOM4]=LoadTexture("GFX/Map/forest/forest4h_mask.png",1+2)

	For i = ROOM1 To ROOM4
		;TextureBlend(mask[i], FE_ALPHAMODULATE)

		fr\tileMesh[i]=load_terrain(hmap[i],0.03,GroundTexture,PathTexture,mask[i])
	Next

	;detail meshes
	;fr\detailMesh[0]=LoadMesh("GFX/Map/forest/detail/860_1_tree1.b3d")
	;fr\detailMesh[1]=LoadMesh("GFX/Map/forest/detail/860_1_tree1_leaves.b3d")
	fr\detailMesh[1]=LoadMesh("GFX/Map/forest/detail/treetest4.b3d");1.b3d)
	;EntityParent(fr\detailMesh[1],fr\detailMesh[0])
	fr\detailMesh[2]=LoadMesh("GFX/Map/forest/detail/rock.b3d")
	fr\detailMesh[3]=LoadMesh("GFX/Map/forest/detail/rock2.b3d")
	fr\detailMesh[4]=LoadMesh("GFX/Map/forest/detail/treetest5.b3d")
	fr\detailMesh[5]=LoadMesh("GFX/Map/forest/wall.b3d")

	For i%=ROOM1 To ROOM4
		HideEntity(fr\tileMesh[i])
	Next
	For i%=1 To 5
		HideEntity(fr\detailMesh[i])
	Next

	tempf3=MeshWidth(fr\tileMesh[ROOM1])
	tempf1=tile_size/tempf3

	For tx%=1 To gridsize-1
		For ty%=1 To gridsize-1
			If (fr\grid[(ty*gridsize)+tx]=1) Then

				tile_type = 0
				If (tx+1<gridsize) Then tile_type = (fr\grid[(ty*gridsize)+tx+1]>0)
				If (tx-1=>0) Then tile_type = tile_type+(fr\grid[(ty*gridsize)+tx-1]>0)

				If (ty+1<gridsize) Then tile_type = tile_type+(fr\grid[((ty+1)*gridsize)+tx]>0)
				If (ty-1=>0) Then tile_type = tile_type+(fr\grid[((ty-1)*gridsize)+tx]>0)

				;fr\grid[(ty*gridsize)+tx]=tile_type

				angle%=0
				Select tile_type
					Case 1
						tile_entity = CopyEntity(fr\tileMesh[ROOM1])

						If (fr\grid[((ty+1)*gridsize)+tx]>0) Then
							angle = 180
						ElseIf (fr\grid[(ty*gridsize)+tx-1]>0) Then
							angle = 270
						ElseIf (fr\grid[(ty*gridsize)+tx+1]>0) Then
							angle = 90
						EndIf

						tile_type = ROOM1
					Case 2
						If (fr\grid[((ty-1)*gridsize)+tx]>0 And fr\grid[((ty+1)*gridsize)+tx]>0) Then
							tile_entity = CopyEntity(fr\tileMesh[ROOM2])
							tile_type = ROOM2
						ElseIf (fr\grid[(ty*gridsize)+tx+1]>0 And fr\grid[(ty*gridsize)+tx-1]>0) Then
							tile_entity = CopyEntity(fr\tileMesh[ROOM2])
							angle = 90
							tile_type = ROOM2
						Else
							tile_entity = CopyEntity(fr\tileMesh[ROOM2C])
							If (fr\grid[(ty*gridsize)+tx-1]>0 And fr\grid[((ty+1)*gridsize)+tx]>0) Then
								angle = 180
							ElseIf (fr\grid[(ty*gridsize)+tx+1]>0 And fr\grid[((ty-1)*gridsize)+tx]>0) Then

							ElseIf (fr\grid[(ty*gridsize)+tx-1]>0 And fr\grid[((ty-1)*gridsize)+tx]>0) Then
								angle = 270
							Else
								angle = 90
							EndIf
							tile_type = ROOM2C
						EndIf
					Case 3
						tile_entity = CopyEntity(fr\tileMesh[ROOM3])

						If (fr\grid[((ty-1)*gridsize)+tx]=0) Then
							angle = 180
						ElseIf (fr\grid[(ty*gridsize)+tx-1]=0) Then
							angle = 90
						ElseIf (fr\grid[(ty*gridsize)+tx+1]=0) Then
							angle = 270
						EndIf

						tile_type = ROOM3
					Case 4
						tile_entity = CopyEntity(fr\tileMesh[ROOM4])
						tile_type = ROOM4
					Default
						DebugLog("tile_type: "+tile_type)
				End Select

				If (tile_type > 0) Then
					;2, 5, 8
					it.Items = Null
					If ((ty Mod 3)=2 And itemPlaced[Floor(ty/3)]=False) Then
						itemPlaced[Floor(ty/3)]=True
						;TODO: Rename the files.
						;it.Items = CreateItem("Log #"+Int(Floor(ty/3)+1), "paper", 0,0.5,0)
						EntityType(it\collider, HIT_ITEM)
						EntityParent(it\collider, tile_entity)
					EndIf

					;place trees and other details
					;only placed on spots where the value of the heightmap is above 100
					SetBuffer(ImageBuffer(hmap[tile_type]))
					width = ImageWidth(hmap[tile_type])
					tempf4# = (tempf3/Float(width))
					For lx = 3 To width-2
						For ly = 3 To width-2
							GetColor(lx,width-ly)

							If (ColorRed()>Rand(100,260)) Then
								Select Rand(0,7)
									Case 0,1,2,3,4,5,6 ;create a tree
										detail_entity=CopyEntity(fr\detailMesh[1])
										;EntityType(detail_entity,HIT_MAP)
										tempf2=Rnd(0.25,0.4)

										For i = 0 To 3
											d% = CopyEntity(fr\detailMesh[4])
											;ScaleEntity(d,tempf2*1.1,tempf2,tempf2*1.1,True)
											RotateEntity(d, 0, 90*i+Rnd(-20,20), 0)
											EntityParent(d,detail_entity)

											EntityFX(d, 1);+8
										Next

										ScaleEntity(detail_entity,tempf2*1.1,tempf2,tempf2*1.1,True)
										PositionEntity(detail_entity,lx*tempf4-(tempf3/2.0),ColorRed()*0.03-Rnd(3.0,3.2),ly*tempf4-(tempf3/2.0),True)

										RotateEntity(detail_entity,Rnd(-5,5),Rnd(360.0),0.0,True)

										;EntityAutoFade(detail_entity,4.0,6.0)
									Case 7 ;add a rock
										detail_entity=CopyEntity(fr\detailMesh[2])
										;EntityType(detail_entity,HIT_MAP)
										tempf2=Rnd(0.01,0.012)
										;ScaleEntity(detail_entity,tempf2,tempf2*Rnd(1.0,2.0),tempf2,True)

										PositionEntity(detail_entity,lx*tempf4-(tempf3/2.0),ColorRed()*0.03-1.3,ly*tempf4-(tempf3/2.0),True)

										EntityFX(detail_entity, 1)

										RotateEntity(detail_entity,0.0,Rnd(360.0),0.0,True)
									Case 6 ;add a stump
										detail_entity=CopyEntity(fr\detailMesh[4])
										;EntityType(detail_entity,HIT_MAP)
										tempf2=Rnd(0.1,0.12)
										ScaleEntity(detail_entity,tempf2,tempf2,tempf2,True)

										PositionEntity(detail_entity,lx*tempf4-(tempf3/2.0),ColorRed()*0.03-1.3,ly*tempf4-(tempf3/2.0),True)
								End Select

								EntityFX(detail_entity, 1)
								;PositionEntity(detail_entity,Rnd(0.0,tempf3)-(tempf3/2.0),ColorRed()*0.03-0.05,Rnd(0.0,tempf3)-(tempf3/2.0),True)
								EntityParent(detail_entity,tile_entity)
							EndIf
						Next
					Next
					SetBuffer(BackBuffer())

					TurnEntity(tile_entity, 0, angle, 0)

					PositionEntity(tile_entity,x+(tx*tile_size),y,z+(ty*tile_size),True)

					ScaleEntity(tile_entity,tempf1,tempf1,tempf1)
					EntityType(tile_entity,HIT_MAP)
					EntityFX(tile_entity,1)
					EntityParent(tile_entity,fr\forest_Pivot)

					If (it<>Null) Then EntityParent(it\collider,0)

					fr\tileEntities[tx+(ty*gridsize)] = tile_entity
				Else
					DebugLog("INVALID TILE @ ("+tx+", "+ty+ "): "+tile_type)
				EndIf
			EndIf

		Next
	Next

	;place the wall
	For i = 0 To 1
		ty = ((gridsize-1)*i)

		For tx = 1 To gridsize-1
			If (fr\grid[(ty*gridsize)+tx]=3) Then
				fr\detailEntities[i]=CopyEntity(fr\detailMesh[5])
				ScaleEntity(fr\detailEntities[i],RoomScale,RoomScale,RoomScale)

				fr\door[i] = CopyEntity(r\objects[3])
				PositionEntity(fr\door[i],72*RoomScale,32.0*RoomScale,0,True)
				RotateEntity(fr\door[i], 0,180,0)
				ScaleEntity(fr\door[i],48*RoomScale,45*RoomScale,48*RoomScale,True)
				EntityParent(fr\door[i],fr\detailEntities[i])
				;SetAnimTime(fr\door[i], 0)

				frame% = CopyEntity(r\objects[2],fr\door[i])
				PositionEntity(frame,0,32.0*RoomScale,0,True)
				ScaleEntity(frame,48*RoomScale,45*RoomScale,48*RoomScale,True)
				EntityParent(frame,fr\detailEntities[i])

				EntityType(fr\detailEntities[i],HIT_MAP)
				;EntityParent(frame,fr\detailEntities[i])

				PositionEntity(fr\detailEntities[i],x+(tx*tile_size),y,z+(ty*tile_size)+(tile_size/2)-(tile_size*i),True)
				RotateEntity(fr\detailEntities[i],0,180*i,0)

				EntityParent(fr\detailEntities[i],fr\forest_Pivot)
			EndIf
		Next
	Next

End Function

Function DestroyForest(fr.Forest)
	Local tx%,ty%
	For tx% = 0 To gridsize-1
		For ty% = 0 To gridsize-1
			If (fr\tileEntities[tx+(ty*gridsize)]<>0) Then
				FreeEntity(fr\tileEntities[tx+(ty*gridsize)])
				fr\tileEntities[tx+(ty*gridsize)] = 0
				fr\grid[tx+(ty*gridsize)] = 0
			EndIf
		Next
	Next
	If (fr\door[0]<>0) Then
		FreeEntity(fr\door[0])
		fr\door[0] = 0
	EndIf
	If (fr\door[1]<>0) Then
		FreeEntity(fr\door[1])
		fr\door[0] = 1
	EndIf
	If (fr\detailEntities[0]<>0) Then
		FreeEntity(fr\detailEntities[0])
		fr\detailEntities[0] = 0
	EndIf
	If (fr\detailEntities[1]<>0) Then
		FreeEntity(fr\detailEntities[1])
		fr\detailEntities[1] = 0
	EndIf

	If (fr\forest_Pivot<>0) Then
		FreeEntity(fr\forest_Pivot)
		fr\forest_Pivot=0
	EndIf
	Local i%
	For i%=0 To 3
		If (fr\tileMesh[i]<>0) Then
			FreeEntity(fr\tileMesh[i])
			fr\tileMesh[i]=0
		EndIf
	Next
	For i%=0 To 4
		If (fr\detailMesh[i]<>0) Then
			FreeEntity(fr\detailMesh[i])
			fr\detailMesh[i]=0
		EndIf
	Next
	For i%=0 To 9
		If (fr\tileTexture[i]<>0) Then
			FreeEntity(fr\tileTexture[i])
			fr\tileTexture[i]=0
		EndIf
	Next

	;Delete fr
End Function


Function UpdateForest(fr.Forest,ent%)
	;local variables
	Local tx%,ty%
	If (Abs(EntityY(ent,True)-EntityY(fr\forest_Pivot,True))<12.0) Then
		For tx% = 0 To gridsize-1
			For ty% = 0 To gridsize-1
				If (fr\tileEntities[tx+(ty*gridsize)]<>0) Then
					If (Abs(EntityX(ent,True)-EntityX(fr\tileEntities[tx+(ty*gridsize)],True))<20.0) Then
						If (Abs(EntityZ(ent,True)-EntityZ(fr\tileEntities[tx+(ty*gridsize)],True))<20.0) Then
							ShowEntity(fr\tileEntities[tx+(ty*gridsize)])
						Else
							HideEntity(fr\tileEntities[tx+(ty*gridsize)])
						EndIf
					Else
						HideEntity(fr\tileEntities[tx+(ty*gridsize)])
					EndIf
				EndIf
			Next
		Next
	EndIf
End Function

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;~IDEal Editor Parameters:
;~C#Blitz3D