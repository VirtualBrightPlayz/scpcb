Function FillRoom_tnnl_maintenance_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x + 2640.0 * RoomScale, -2496.0 * RoomScale, r\z + 400.0 * RoomScale)
    EntityParent(r\Objects[0], r\obj)
    
    r\Objects[1] = CreatePivot()
    PositionEntity(r\Objects[1], r\x - 4336.0 * RoomScale, -2496.0 * RoomScale, r\z - 2512.0 * RoomScale)
    EntityParent(r\Objects[1], r\obj)
    
    r\Objects[2] = CreatePivot()
    RotateEntity r\Objects[2],0.0,180.0,0.0,True
    PositionEntity(r\Objects[2], r\x + 552.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale)
    EntityParent(r\Objects[2], r\obj)
;			
    r\Objects[4] = CreatePivot()
    PositionEntity(r\Objects[4], r\x - 552.0 * RoomScale, 240.0 * RoomScale, r\z - 656.0 * RoomScale)
    EntityParent(r\Objects[4], r\obj)
;			
    r\RoomDoors[0] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 224.0 * RoomScale, 0.7, r\z + 480.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 304.0 * RoomScale, 0.7, r\z + 832.0 * RoomScale, True)			
    
    r\RoomDoors[2] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0.0, r\z - 656.0 * RoomScale, 90, r, True)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = True
    PositionEntity(r\RoomDoors[2]\buttons[0], r\x - 224.0 * RoomScale, 0.7, r\z - 480.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[2]\buttons[1], r\x - 304.0 * RoomScale, 0.7, r\z - 832.0 * RoomScale, True)
;			
    temp = ((Int(AccessCode)*3) Mod 10000)
    If temp < 1000 Then temp = temp+1000
    d.Doors = CreateDoor(0, r\x,r\y,r\z,0, r, False, True, False, temp)
    PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, r\y + 0.7, r\z - 384.0 * RoomScale, True)
    RotateEntity (d\buttons[0], 0,-90,0,True)
    PositionEntity(d\buttons[1], r\x - 224.0 * RoomScale, r\y + 0.7, r\z + 384.0 * RoomScale, True)		
    RotateEntity (d\buttons[1], 0,90,0,True)
    
    de.Decals = CreateDecal(0, r\x + 64.0 * RoomScale, 0.005, r\z + 144.0 * RoomScale, 90, Rand(360), 0)
    EntityParent(de\obj, r\obj)
    it = CreateItem("Scorched Note", "paper", r\x + 64.0 * RoomScale, r\y +144.0 * RoomScale, r\z - 384.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_tnnl_maintenance_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	
	If EntityY(mainPlayer\collider,True)>=8.0 And EntityY(mainPlayer\collider,True)<=12.0 Then
		If (EntityX(mainPlayer\collider,True)>=e\room\x-6.0) And (EntityX(mainPlayer\collider,True)<=(e\room\x+2.0*gridsz+6.0)) Then
			If (EntityZ(mainPlayer\collider,True)>=e\room\z-6.0) And (EntityZ(mainPlayer\collider,True)<=(e\room\z+2.0*gridsz+6.0)) Then
				mainPlayer\currRoom=e\room
			EndIf
		EndIf
	EndIf
	
	If mainPlayer\currRoom = e\room Then
		
		Local Meshes%[7]
		Local tempStr$
		
		Local ia%,ib%,ic%,id%
		Local dr.Doors
		
		Local tempInt%,tempInt2%
		Local ix%,iy%
		
		If e\room\grid = Null Then
			
			e\room\grid=New Grids
			
			Local dir%
			
			dir=Rand(0,1) Shl 1
			;0 = right
			;1 = up
			;2 = left
			;3 = down
			
			ix=gridsz/2+Rand(-2,2)
			iy=gridsz/2+Rand(-2,2)
			
			e\room\grid\grid[ix+(iy*gridsz)]=1
			
			If dir=2 Then e\room\grid\grid[(ix+1)+(iy*gridsz)]=1 Else e\room\grid\grid[(ix-1)+(iy*gridsz)]=1
			
			Local count% = 2
			
			While count<100
				tempInt=Rand(1,5) Shl Rand(1,2)
				For i=1 To tempInt
					
					tempInt2=True
					
					Select dir
						Case 0
							If ix<gridsz-2-(i Mod 2) Then ix=ix+1 Else tempInt2=False
						Case 1
							If iy<gridsz-2-(i Mod 2) Then iy=iy+1 Else tempInt2=False
						Case 2
							If ix>1+(i Mod 2) Then ix=ix-1 Else tempInt2=False
						Case 3
							If iy>1+(i Mod 2) Then iy=iy-1 Else tempInt2=False
					End Select
					
					If tempInt2 Then
						If e\room\grid\grid[ix+(iy*gridsz)]=0 Then
							e\room\grid\grid[ix+(iy*gridsz)]=1
							count=count+1
						EndIf
					Else
						Exit
					EndIf
				Next
				dir=dir+((Rand(0,1) Shl 1)-1)
				While dir<0
					dir=dir+4
				Wend
				While dir>3
					dir=dir-4
				Wend
			Wend
			
			;generate the tunnels
			For iy=0 To gridsz-1
				For ix=0 To gridsz-1
					If e\room\grid\grid[ix+(iy*gridsz)]>0 Then
						e\room\grid\grid[ix+(iy*gridsz)]=(e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0)+(e\room\grid\grid[(ix)+((iy-1)*gridsz)]>0)+(e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0)+(e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0)
					EndIf
				Next
			Next
			
			Local maxX%=gridsz-1
			Local canRetry%=0
			
			For ix=0 To maxX
				For iy=0 To gridsz-1
					If e\room\grid\grid[ix+1+(iy*gridsz)]>0 Then
						maxX=ix
						If (e\room\grid\grid[ix+1+((iy+1)*gridsz)]<3) And (e\room\grid\grid[ix+1+((iy-1)*gridsz)]<3) Then
							canRetry=1
							If Rand(0,1)=1 Then
								e\room\grid\grid[ix+1+((iy)*gridsz)]=e\room\grid\grid[ix+1+((iy)*gridsz)]+1
								e\room\grid\grid[ix+((iy)*gridsz)]=7 ;generator room
								canRetry=0
								Exit
							EndIf
						EndIf
					EndIf
				Next
				If canRetry Then ix=ix-1
			Next
			
			Local firstX%,lastX%
			Local firstY%,lastY%
			
			firstX=-1
			lastY=-1
			firstX=-1
			lastY=-1
			
			For iy=0 To gridsz-1
				For ix=0 To gridsz-1
					If e\room\grid\grid[ix+(iy*gridsz)]=2 Then
						If e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0 And e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0 Then ;horizontal
							If firstX=-1 Or firstY=-1 Then
								If e\room\grid\grid[ix-1+(iy*gridsz)]<3 And e\room\grid\grid[ix+1+(iy*gridsz)]<3 And e\room\grid\grid[ix+((iy-1)*gridsz)]<3 And e\room\grid\grid[ix+((iy+1)*gridsz)]<3 Then
									If e\room\grid\grid[ix-1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix-1+((iy+1)*gridsz)]<1 Then
										firstX=ix : firstY=iy
									EndIf
								EndIf
							EndIf
							If e\room\grid\grid[ix-1+(iy*gridsz)]<3 And e\room\grid\grid[ix+1+(iy*gridsz)]<3 And e\room\grid\grid[ix+((iy-1)*gridsz)]<3 And e\room\grid\grid[ix+((iy+1)*gridsz)]<3 Then
								If e\room\grid\grid[ix-1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix-1+((iy+1)*gridsz)]<1 Then
									lastX=ix : lastY=iy
								EndIf
							EndIf
						ElseIf e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0 And e\room\grid\grid[(ix)+((iy-1)*gridsz)]>0 Then ;vertical
							If firstX=-1 Or firstY=-1 Then
								If e\room\grid\grid[ix-1+(iy*gridsz)]<3 And e\room\grid\grid[ix+1+(iy*gridsz)]<3 And e\room\grid\grid[ix+((iy-1)*gridsz)]<3 And e\room\grid\grid[ix+((iy+1)*gridsz)]<3 Then
									If e\room\grid\grid[ix-1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[x-1+((iy+1)*gridsz)]<1 Then
										firstX=ix : firstY=iy
									EndIf
								EndIf
							EndIf
							If e\room\grid\grid[ix-1+(iy*gridsz)]<3 And e\room\grid\grid[ix+1+(iy*gridsz)]<3 And e\room\grid\grid[ix+((iy-1)*gridsz)]<3 And e\room\grid\grid[ix+((iy+1)*gridsz)]<3 Then
								If e\room\grid\grid[ix-1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix-1+((iy+1)*gridsz)]<1 Then
									lastX=ix : lastY=iy
								EndIf
							EndIf
						EndIf
					EndIf
				Next
			Next
			
			If lastX=firstX And lastY=firstY Then
				RuntimeError("The maintenance tunnels could not be generated properly!")
			EndIf
			
			;place the tunnels
			
			For i=0 To 4
				Select True
					Case i=2
						tempStr="2c"
					Case i>2
						tempStr=Str(i)
					Default
						tempStr=Str(i+1)
				End Select
				Meshes[i]=LoadRMesh("GFX\map\mt"+tempStr+".rmesh",Null)
				DebugLog i
				HideEntity Meshes[i]
			Next
			
			Meshes[5]=LoadRMesh("GFX\map\mt_elevator.rmesh",Null)
			HideEntity Meshes[5]
			Meshes[6]=LoadRMesh("GFX\map\mt_generator.rmesh",Null)
			HideEntity Meshes[6]
			
			FreeTextureCache
			
			tempInt=0
			
			For iy=0 To gridsz-1
				For ix=0 To gridsz-1
					If e\room\grid\grid[ix+(iy*gridsz)]>0 Then
						
						Select e\room\grid\grid[ix+(iy*gridsz)]
							Case 1,7
								
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
								
								If e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0 Then
									RotateEntity tempInt,0,90,0
									e\room\grid\angles[ix+(iy*gridsz)]=1
								ElseIf e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0 Then
									RotateEntity tempInt,0,270,0
									e\room\grid\angles[ix+(iy*gridsz)]=3
								ElseIf e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0 Then
									RotateEntity tempInt,0,180,0
									e\room\grid\angles[ix+(iy*gridsz)]=2
								Else
									RotateEntity tempInt,0,0,0
									e\room\grid\angles[ix+(iy*gridsz)]=0
								EndIf
							Case 2
								
								If (ix=firstX And iy=firstY) Or (ix=lastX And iy=lastY) Then
									e\room\grid\grid[ix+(iy*gridsz)]=6
								EndIf
								
								If e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0 And e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0 Then ;horizontal
									tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
									
									AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
									
									tempInt2=Rand(0,1)
									RotateEntity tempInt,0.0,tempInt2*180.0+90,0.0
									
									e\room\grid\angles[ix+(iy*gridsz)]=(tempInt2*2)+1
								ElseIf e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0 And e\room\grid\grid[(ix)+((iy-1)*gridsz)]>0 Then ;vertical
									tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
									
									AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
									
									tempInt2=Rand(0,1)
									RotateEntity tempInt,0.0,tempInt2*180.0,0.0
									e\room\grid\angles[ix+(iy*gridsz)]=(tempInt2*2)
								Else
									tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
									
									AddLight%(Null, e\room\x+ix*2.0, 8.0+(412.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
									
									ia=e\room\grid\grid[(ix)+((iy+1)*gridsz)]
									ib=e\room\grid\grid[(ix)+((iy-1)*gridsz)]
									ic=e\room\grid\grid[(ix+1)+((iy)*gridsz)]
									id=e\room\grid\grid[(ix-1)+((iy)*gridsz)]
									
									If ia>0 And ic>0 Then
										RotateEntity tempInt,0,0,0
										e\room\grid\angles[ix+(iy*gridsz)]=0
									ElseIf ia>0 And id>0 Then
										RotateEntity tempInt,0,90,0
										e\room\grid\angles[ix+(iy*gridsz)]=1
									ElseIf ib>0 And ic>0 Then
										RotateEntity tempInt,0,270,0
										e\room\grid\angles[ix+(iy*gridsz)]=3
									Else
										RotateEntity tempInt,0,180,0
										e\room\grid\angles[ix+(iy*gridsz)]=2
									EndIf
								EndIf
								
								If (ix=firstX And iy=firstY) Then
									e\room\grid\grid[ix+(iy*gridsz)]=5
								EndIf
								
							Case 3
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
								
								ia=e\room\grid\grid[(ix)+((iy+1)*gridsz)]
								ib=e\room\grid\grid[(ix)+((iy-1)*gridsz)]
								ic=e\room\grid\grid[(ix+1)+((iy)*gridsz)]
								id=e\room\grid\grid[(ix-1)+((iy)*gridsz)]
								If ia>0 And ic>0 And id>0 Then
									RotateEntity tempInt,0,90,0
									e\room\grid\angles[ix+(iy*gridsz)]=1
								ElseIf ib>0 And ic>0 And id>0 Then
									RotateEntity tempInt,0,270,0
									e\room\grid\angles[ix+(iy*gridsz)]=3
								ElseIf ic>0 And ia>0 And ib>0 Then
									RotateEntity tempInt,0,0,0
									e\room\grid\angles[ix+(iy*gridsz)]=0
								Else
									RotateEntity tempInt,0,180,0
									e\room\grid\angles[ix+(iy*gridsz)]=2
								EndIf
							Case 4
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
								
								tempInt2=Rand(0,3)
								RotateEntity tempInt,0,tempInt2*90.0,0
								
								e\room\grid\angles[ix+(iy*gridsz)]=tempInt2
						End Select
						
						ScaleEntity tempInt,RoomScale,RoomScale,RoomScale,True
						PositionEntity tempInt,e\room\x+ix*2.0,8.0,e\room\z+iy*2.0,True
						
						Select e\room\grid\grid[ix+(iy*gridsz)]
							Case 1;,5,6
								AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0, 255, 255, 255)
							Case 3,4
								AddLight%(Null, e\room\x+ix*2.0, 8.0+(412.0*RoomScale), e\room\z+iy*2.0, 2, 500.0, 255, 255, 255)
							Case 7
								AddLight%(Null, e\room\x+ix*2.0-(Sin(EntityYaw(tempInt,True))*504.0*RoomScale)+(Cos(EntityYaw(tempInt,True))*16.0*RoomScale), 8.0+(396.0*RoomScale), e\room\z+iy*2.0+(Cos(EntityYaw(tempInt,True))*504.0*RoomScale)+(Sin(EntityYaw(tempInt,True))*16.0*RoomScale), 2, 500.0 * RoomScale, 255, 200, 200)
								it = CreateItem("SCP-500-01","scp500",e\room\x+ix*2.0+(Cos(EntityYaw(tempInt,True))*(-208.0)*RoomScale)-(Sin(EntityYaw(tempInt,True))*1226.0*RoomScale),8.0+(80.0*RoomScale),e\room\z+iy*2.0+(Sin(EntityYaw(tempInt,True))*(-208.0)*RoomScale)+(Cos(EntityYaw(tempInt,True))*1226.0*RoomScale))
								EntityType (it\collider, HIT_ITEM)
								
								it = CreateItem("Night Vision Goggles", "nvgoggles",e\room\x+ix*2.0-(Sin(EntityYaw(tempInt,True))*504.0*RoomScale)+(Cos(EntityYaw(tempInt,True))*16.0*RoomScale), 8.0+(80.0*RoomScale), e\room\z+iy*2.0+(Cos(EntityYaw(tempInt,True))*504.0*RoomScale)+(Sin(EntityYaw(tempInt,True))*16.0*RoomScale))
								EntityType (it\collider, HIT_ITEM)
						End Select
						
						If e\room\grid\grid[ix+(iy*gridsz)]=6 Or e\room\grid\grid[ix+(iy*gridsz)]=5 Then
							dr=CreateDoor(e\room\zone,e\room\x+(ix*2.0)+(Cos(EntityYaw(tempInt,True))*240.0*RoomScale),8.0,e\room\z+(iy*2.0)+(Sin(EntityYaw(tempInt,True))*240.0*RoomScale),EntityYaw(tempInt,True)+90.0,Null,False,False,False,"")
							PositionEntity dr\buttons[0],EntityX(dr\buttons[0],True)+(Cos(EntityYaw(tempInt,True))*0.05),EntityY(dr\buttons[0],True)+0.0,EntityZ(dr\buttons[0],True)+(Sin(EntityYaw(tempInt,True))*0.05),True
							
							AddLight%(Null, e\room\x+ix*2.0+(Cos(EntityYaw(tempInt,True))*555.0*RoomScale), 8.0+(469.0*RoomScale), e\room\z+iy*2.0+(Sin(EntityYaw(tempInt,True))*555.0*RoomScale), 2, 600.0 * RoomScale, 255, 255, 255)
							
							tempInt2=CreatePivot()
							RotateEntity tempInt2,0,EntityYaw(tempInt,True)+180.0,0,True
							PositionEntity tempInt2,e\room\x+(ix*2.0)+(Cos(EntityYaw(tempInt,True))*552.0*RoomScale),8.0+(240.0*RoomScale),e\room\z+(iy*2.0)+(Sin(EntityYaw(tempInt,True))*552.0*RoomScale)
							If e\room\grid\grid[ix+(iy*gridsz)]=6 Then
								e\room\RoomDoors[1]=dr
								e\room\Objects[3]=tempInt2
							Else
								e\room\RoomDoors[3]=dr
								e\room\Objects[5]=tempInt2
							EndIf
						EndIf
						
						e\room\grid\Entities[ix+(iy*gridsz)]=tempInt
						
						wayp.WayPoints = CreateWaypoint(e\room\x+(ix*2.0),8.2,e\room\z+(iy*2.0),Null,e\room)
						
						e\room\grid\waypoints[ix+(iy*gridsz)]=wayp
						
						If iy<gridsz-1 Then
							If e\room\grid\waypoints[ix+((iy+1)*gridsz)]<>Null Then
								dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+((iy+1)*gridsz)]\obj)
								For i=0 To 3
									If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy+1)*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy+1)*gridsz)]
										e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
								For i=0 To 3
									If e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
										e\room\grid\waypoints[ix+((iy+1)*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
							EndIf
						EndIf
						If iy>0 Then
							If e\room\grid\waypoints[ix+((iy-1)*gridsz)]<>Null Then
								dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+((iy-1)*gridsz)]\obj)
								For i=0 To 3
									If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy-1)*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy-1)*gridsz)]
										e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
								For i=0 To 3
									If e\room\grid\waypoints[ix+((iy-1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+((iy-1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
										e\room\grid\waypoints[ix+((iy-1)*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
							EndIf
						EndIf
						If ix>0 Then
							If e\room\grid\waypoints[ix-1+(iy*gridsz)]<>Null Then
								dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix-1+(iy*gridsz)]\obj)
								For i=0 To 3
									If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix-1+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix-1+(iy*gridsz)]
										e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
								For i=0 To 3
									If e\room\grid\waypoints[ix-1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix-1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
										e\room\grid\waypoints[ix-1+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
							EndIf
						EndIf
						If ix<gridsz-1 Then
							If e\room\grid\waypoints[ix+1+(iy*gridsz)]<>Null Then
								dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+1+(iy*gridsz)]\obj)
								For i=0 To 3
									If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+1+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+1+(iy*gridsz)]
										e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
								For i=0 To 3
									If e\room\grid\waypoints[ix+1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
										e\room\grid\waypoints[ix+1+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
							EndIf
						EndIf
						
					EndIf
				Next
			Next
			
			For i=0 To 6
				e\room\grid\Meshes[i]=Meshes[i]
				;FreeEntity Meshes[i]
			Next
			
			PositionEntity e\room\Objects[0],e\room\x+firstX*2.0,8.0,e\room\z+firstY*2.0,True
			PositionEntity e\room\Objects[1],e\room\x+lastX*2.0,8.0,e\room\z+lastY*2.0,True
			
		Else If e\room\grid\Meshes[0]=0 Then
			
			;place the tunnels
			
			For i=0 To 4
				Select True
					Case i=2
						tempStr="2c"
					Case i>2
						tempStr=Str(i)
					Default
						tempStr=Str(i+1)
				End Select
				Meshes[i]=LoadRMesh("GFX\map\mt"+tempStr+".rmesh",Null)
				DebugLog i
				HideEntity Meshes[i]
			Next
			
			Meshes[5]=LoadRMesh("GFX\map\mt_elevator.rmesh",Null)
			HideEntity Meshes[5]
			Meshes[6]=LoadRMesh("GFX\map\mt_generator.rmesh",Null)
			HideEntity Meshes[6]
			
			FreeTextureCache
			
			tempInt=0
			
			For iy=0 To gridsz-1
				For ix=0 To gridsz-1
					If e\room\grid\grid[ix+(iy*gridsz)]>0 Then
						Select e\room\grid\grid[ix+(iy*gridsz)]
							Case 1,7
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
							Case 2
								If e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0 And e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0 Then ;horizontal
									tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
									AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
								ElseIf e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0 And e\room\grid\grid[(ix)+((iy-1)*gridsz)]>0 Then ;vertical
									tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
									AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
								Else
									tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
									AddLight%(Null, e\room\x+ix*2.0, 8.0+(412.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
								EndIf
							Case 3,4
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
							Case 5,6
								tempInt%=CopyEntity(Meshes[5])
						End Select
						
						ScaleEntity tempInt,RoomScale,RoomScale,RoomScale,True
						
						RotateEntity tempInt,0,e\room\grid\angles[ix+(iy*gridsz)]*90.0,0
						PositionEntity tempInt,e\room\x+ix*2.0,8.0,e\room\z+iy*2.0,True
						
						Select e\room\grid\grid[ix+(iy*gridsz)]
							Case 1,5,6
								AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
							Case 3,4
								AddLight%(Null, e\room\x+ix*2.0, 8.0+(412.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
							Case 7
								AddLight%(Null, e\room\x+ix*2.0-(Sin(EntityYaw(tempInt,True))*504.0*RoomScale)+(Cos(EntityYaw(tempInt,True))*16.0*RoomScale), 8.0+(396.0*RoomScale), e\room\z+iy*2.0+(Cos(EntityYaw(tempInt,True))*504.0*RoomScale)+(Sin(EntityYaw(tempInt,True))*16.0*RoomScale), 2, 500.0 * RoomScale, 255, 200, 200)
						End Select
						
						If e\room\grid\grid[ix+(iy*gridsz)]=6 Or e\room\grid\grid[ix+(iy*gridsz)]=5 Then
							dr=CreateDoor(e\room\zone,e\room\x+(ix*2.0)+(Cos(EntityYaw(tempInt,True))*240.0*RoomScale),8.0,e\room\z+(iy*2.0)+(Sin(EntityYaw(tempInt,True))*240.0*RoomScale),EntityYaw(tempInt,True)+90.0,Null,False,False,False,"")
							
							AddLight%(Null, e\room\x+ix*2.0+(Cos(EntityYaw(tempInt,True))*555.0*RoomScale), 8.0+(469.0*RoomScale), e\room\z+iy*2.0+(Sin(EntityYaw(tempInt,True))*555.0*RoomScale), 2, 600.0 * RoomScale, 255, 255, 255)
							
							PositionEntity dr\buttons[0],EntityX(dr\buttons[0],True)+(Cos(EntityYaw(tempInt,True))*0.05),EntityY(dr\buttons[0],True)+0.0,EntityZ(dr\buttons[0],True)+(Sin(EntityYaw(tempInt,True))*0.05),True
							tempInt2=CreatePivot()
							RotateEntity tempInt2,0,EntityYaw(tempInt,True)+180.0,0,True
							PositionEntity tempInt2,e\room\x+(ix*2.0)+(Cos(EntityYaw(tempInt,True))*552.0*RoomScale),8.0+(240.0*RoomScale),e\room\z+(iy*2.0)+(Sin(EntityYaw(tempInt,True))*552.0*RoomScale)
							If e\room\grid\grid[ix+(iy*gridsz)]=6 Then
								dr\open = (Not e\room\RoomDoors[0]\open)
								e\room\RoomDoors[1]=dr
								e\room\Objects[3]=tempInt2
								PositionEntity e\room\Objects[1],e\room\x+ix*2.0,8.0,e\room\z+iy*2.0,True
							Else
								dr\open = (Not e\room\RoomDoors[2]\open)
								e\room\RoomDoors[3]=dr
								e\room\Objects[5]=tempInt2
								PositionEntity e\room\Objects[0],e\room\x+ix*2.0,8.0,e\room\z+iy*2.0,True
							EndIf
						EndIf
						
						e\room\grid\Entities[ix+(iy*gridsz)]=tempInt
						
						wayp.WayPoints = CreateWaypoint(e\room\x+(ix*2.0),8.2,e\room\z+(iy*2.0),Null,e\room)
						
						e\room\grid\waypoints[ix+(iy*gridsz)]=wayp
						
						If iy<gridsz-1 Then
							If e\room\grid\waypoints[ix+((iy+1)*gridsz)]<>Null Then
								dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+((iy+1)*gridsz)]\obj)
								For i=0 To 3
									If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy+1)*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy+1)*gridsz)]
										e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
								For i=0 To 3
									If e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
										e\room\grid\waypoints[ix+((iy+1)*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
							EndIf
						EndIf
						If iy>0 Then
							If e\room\grid\waypoints[ix+((iy-1)*gridsz)]<>Null Then
								dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+((iy-1)*gridsz)]\obj)
								For i=0 To 3
									If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy-1)*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy-1)*gridsz)]
										e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
								For i=0 To 3
									If e\room\grid\waypoints[ix+((iy-1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+((iy-1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
										e\room\grid\waypoints[ix+((iy-1)*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
							EndIf
						EndIf
						If ix>0 Then
							If e\room\grid\waypoints[ix-1+(iy*gridsz)]<>Null Then
								dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix-1+(iy*gridsz)]\obj)
								For i=0 To 3
									If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix-1+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix-1+(iy*gridsz)]
										e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
								For i=0 To 3
									If e\room\grid\waypoints[ix-1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix-1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
										e\room\grid\waypoints[ix-1+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
							EndIf
						EndIf
						If ix<gridsz-1 Then
							If e\room\grid\waypoints[ix+1+(iy*gridsz)]<>Null Then
								dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+1+(iy*gridsz)]\obj)
								For i=0 To 3
									If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+1+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+1+(iy*gridsz)]
										e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
								For i=0 To 3
									If e\room\grid\waypoints[ix+1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
										Exit
									ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
										e\room\grid\waypoints[ix+1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
										e\room\grid\waypoints[ix+1+(iy*gridsz)]\dist[i]=dist
										Exit
									EndIf
								Next
							EndIf
						EndIf
					EndIf
				Next
			Next
			
			For i=0 To 6
				e\room\grid\Meshes[i]=Meshes[i]
			Next
			
			For it.Items = Each Items
				If (EntityY(it\collider,True)>=8.0) And (EntityY(it\collider,True)<=12.0) Then
					DebugLog it\name+" is within Y limits"
					If (EntityX(it\collider,True)>=e\room\x-6.0) And (EntityX(it\collider,True)<=(e\room\x+(2.0*gridsz)+6.0)) Then
						DebugLog "and within X limits"
					EndIf
					If (EntityZ(it\collider,True)>=e\room\z-6.0) And (EntityZ(it\collider,True)<=(e\room\z+(2.0*gridsz)+6.0)) Then
						DebugLog "and within Z limits"
					EndIf
				EndIf
				
				If (EntityY(it\collider,True)>=8.0) And (EntityY(it\collider,True)<=12.0) And (EntityX(it\collider,True)>=e\room\x-6.0) And (EntityX(it\collider,True)<=(e\room\x+(2.0*gridsz)+6.0)) And (EntityZ(it\collider,True)>=e\room\z-6.0) And (EntityZ(it\collider,True)<=(e\room\z+(2.0*gridsz)+6.0)) Then
					DebugLog it\name
					TranslateEntity it\collider,0.0,0.3,0.0,True
					ResetEntity it\collider
				EndIf
			Next
			
		EndIf
		
		If EntityY(mainPlayer\collider,True)>4.0 Then
			If Music(7)=0 Then Music(7) = LoadSound_Strict("SFX\Music\Room3Storage.ogg") 
			ShouldPlay = 7
			
			If e\EventState = 0 Then
				If EntityDistance(mainPlayer\collider, e\room\Objects[0]) < EntityDistance(mainPlayer\collider, e\room\Objects[1]) Then
					temp = 0
				Else
					temp = 1
				EndIf
				e\EventState = 2
				
				If (Not Contained106) Then 	
					de.Decals = CreateDecal(0, EntityX(e\room\Objects[temp],True), EntityY(e\room\Objects[temp],True)+0.05, EntityZ(e\room\Objects[temp],True), 90, Rand(360), 0)
					de\Size = 0.05 : de\SizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals
					
					DebugLog "updateevents collidey: "+EntityY(mainPlayer\collider,True)
					PositionEntity Curr106\Collider, EntityX(e\room\Objects[temp],True), EntityY(mainPlayer\collider,True)-3.0, EntityZ(e\room\Objects[temp],True)
					SetAnimTime Curr106\obj, 110
					Curr106\State = -0.1	
					Curr106\PrevY = EntityY(mainPlayer\collider)
				EndIf
				
				For i = 0 To 4
					Local spawnPoint.WayPoints = Null
					For x = i*((gridsz*gridsz)/5.0) To (gridsz*gridsz-1)
						DebugLog("spawn 966 X: "+x)
						If Rand(2)=1 And e\room\grid\waypoints[x]<>Null Then 
							spawnPoint = e\room	\grid\waypoints[x]
							x = gridsz*gridsz
						EndIf
					Next 
					If (spawnPoint<>Null) Then
						e\room\NPC[i] =CreateNPC(NPCtype966, EntityX(spawnPoint\obj,True), EntityY(spawnPoint\obj,True), EntityZ(spawnPoint\obj,True))
					EndIf
				Next
			EndIf
		EndIf ;entityy(collider) >4
		
		e\EventState2 = UpdateElevators2(e\EventState2, e\room\RoomDoors[0], e\room\RoomDoors[1],e\room\Objects[2],e\room\Objects[3], e)
		e\EventState3 = UpdateElevators2(e\EventState3, e\room\RoomDoors[2], e\room\RoomDoors[3],e\room\Objects[4],e\room\Objects[5], e)
		
	EndIf 
	;[End Block]
End Function

