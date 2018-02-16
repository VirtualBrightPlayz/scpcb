

Type Materials
	Field name$
	Field Diff
	;Field Bump
	
	Field StepSound%
End Type

Function LoadMaterials(file$)
	Local TemporaryString$
	Local mat.Materials = Null
	Local StrTemp$ = ""
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			
			mat.Materials = New Materials
			
			mat\name = Lower(TemporaryString)
			
			;If BumpEnabled Then
			;	StrTemp = GetINIString(file, TemporaryString, "bump")
			;	If StrTemp <> "" Then 
			;		mat\Bump =  LoadTexture_Strict(StrTemp)
			;		
			;		TextureBlend mat\Bump, FE_BUMP				
			;	EndIf
			;EndIf
			
			mat\StepSound = (GetINIInt(file, TemporaryString, "stepsound")+1)
		EndIf
	Wend
	
	CloseFile f
	
End Function

;RMESH STUFF;;;;

Function StripFilename$(file$)
	Local mi$=""
	Local lastSlash%=0
	If Len(file)>0
		For i%=1 To Len(file)
			mi=Mid(file$,i,1)
			If mi="\" Or mi="/" Then
				lastSlash=i
			EndIf
		Next
	EndIf
	
	Return Left(file,lastSlash)
End Function

Function GetTextureFromCache%(name$)
	For tc.Materials=Each Materials
		If tc\name = name Then Return tc\Diff
	Next
	Return 0
End Function

;Function GetBumpFromCache%(name$)
;	For tc.Materials=Each Materials
;		If tc\name = name Then Return tc\Bump
;	Next
;	Return 0
;End Function

Function GetCache.Materials(name$)
	For tc.Materials=Each Materials
		If tc\name = name Then Return tc
	Next
	Return Null
End Function

Function AddTextureToCache(texture%)
	Local tc.Materials=GetCache(StripPath(TextureName(texture)))
	If tc.Materials=Null Then
		tc.Materials=New Materials
		tc\name=StripPath(TextureName(texture))
		;Local temp$=GetINIString("Data\materials.ini",tc\name,"bump")
		;If temp<>"" Then
		;	tc\Bump=LoadTexture_Strict(temp)
		;	TextureBlend tc\Bump,FE_BUMP
		;Else
		;	tc\Bump=0
		;EndIf
		tc\Diff=0
	EndIf
	If tc\Diff=0 Then tc\Diff=texture
End Function

Function ClearTextureCache()
	For tc.Materials=Each Materials
		If tc\Diff<>0 Then FreeTexture tc\Diff
		;If tc\Bump<>0 Then FreeTexture tc\Bump
		Delete tc
	Next
End Function

Function FreeTextureCache()
	For tc.Materials=Each Materials
		If tc\Diff<>0 Then FreeTexture tc\Diff
		;If tc\Bump<>0 Then FreeTexture tc\Bump
		tc\Diff = 0; : tc\Bump = 0
	Next
End Function

Function LoadRMesh(file$,rt.RoomTemplates)
	;generate a texture made of white
	Local blankTexture%
	blankTexture=CreateTexture(4,4,1,1)
	ClsColor 255,255,255
	SetBuffer TextureBuffer(blankTexture)
	Cls
	SetBuffer BackBuffer()
	
	Local pinkTexture%
	pinkTexture=CreateTexture(4,4,1,1)
	ClsColor 255,255,255
	SetBuffer TextureBuffer(pinkTexture)
	Cls
	SetBuffer BackBuffer()
	
	ClsColor 0,0,0
	
	;read the file
	Local f%=ReadFile(file)
	Local i%,j%,k%,x#,y#,z#,yaw#
	Local vertex%
	Local temp1i%,temp2i%,temp3i%
	Local temp1#,temp2#,temp3#
	Local temp1s$, temp2s$
	
	Local collisionMeshes% = CreatePivot()
	
	Local hasTriggerBox% = False
	
	For i=0 To 3 ;reattempt up to 3 times
		If f=0 Then
			f=ReadFile(file)
		Else
			Exit
		EndIf
	Next
	If f=0 Then RuntimeError "Error reading file "+Chr(34)+file+Chr(34)
	Local isRMesh$ = ReadString(f)
	If isRMesh$="RoomMesh"
		;Continue
	ElseIf isRMesh$="RoomMesh.HasTriggerBox"
		;RuntimeError Chr(34)+file+Chr(34)+" has the triggerbox header, therefore making it an invalid RMesh file. Clean this shit up."
		hasTriggerBox% = True
	Else
		RuntimeError Chr(34)+file+Chr(34)+" is Not RMESH ("+isRMesh+")"
	EndIf
	
	file=StripFilename(file)
	
	Local count%,count2%
	
	;drawn meshes
	Local Opaque%,Alpha%
	
	Opaque=CreateMesh()
	Alpha=CreateMesh()
	
	count = ReadInt(f)
	Local childMesh%
	Local surf%,tex%[2],brush%
	
	Local isAlpha%
	
	Local u#,v#
	
	For i=1 To count ;drawn mesh
		childMesh=CreateMesh()
		
		surf=CreateSurface(childMesh)
		
		brush=CreateBrush()
		
		tex[0]=0 : tex[1]=0
		
		isAlpha=0
		For j=0 To 1
			temp1i=ReadByte(f)
			If temp1i<>0 Then
				temp1s=ReadString(f)
				tex[j]=GetTextureFromCache(temp1s)
				If tex[j]=0 Then ;texture is not in cache
					Select True
						Case temp1i<3
							tex[j]=LoadTexture(file+temp1s,1)
						Default
							tex[j]=LoadTexture(file+temp1s,3)
					End Select
					
					If tex[j]<>0 Then
						If temp1i=1 Then TextureBlend tex[j],5
						If Instr(Lower(temp1s),"_lm")<>0 Then
							TextureBlend tex[j],3
						EndIf
						AddTextureToCache(tex[j])
					EndIf
					
				EndIf
				If tex[j]<>0 Then
					isAlpha=2
					If temp1i=3 Then isAlpha=1
					
					TextureCoords tex[j],1-j
				EndIf
			EndIf
		Next
		
		If isAlpha=1 Then
			If tex[1]<>0 Then
				TextureBlend tex[1],2
				BrushTexture brush,tex[1],0,0
			Else
				BrushTexture brush,blankTexture,0,0
			EndIf
		Else
			
			If tex[0]<>0 And tex[1]<>0 Then
				For j=0 To 1
					BrushTexture brush,tex[j],0,j+1
				Next
				BrushTexture brush,AmbientLightRoomTex,0
			Else
				For j=0 To 1
					If tex[j]<>0 Then
						BrushTexture brush,tex[j],0,j
					Else
						BrushTexture brush,blankTexture,0,j
					EndIf
				Next
			EndIf
		EndIf
		
		surf=CreateSurface(childMesh)
		
		If isAlpha>0 Then PaintSurface surf,brush
		
		FreeBrush brush : brush = 0
		
		count2=ReadInt(f) ;vertices
		
		For j%=1 To count2
			;world coords
			x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
			vertex=AddVertex(surf,x,y,z)
			
			;texture coords
			For k%=0 To 1
				u=ReadFloat(f) : v=ReadFloat(f)
				VertexTexCoords surf,vertex,u,v,0.0,k
			Next
			
			;colors
			temp1i=ReadByte(f)
			temp2i=ReadByte(f)
			temp3i=ReadByte(f)
			VertexColor surf,vertex,temp1i,temp2i,temp3i,1.0
		Next
		
		count2=ReadInt(f) ;polys
		For j%=1 To count2
			temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
			AddTriangle(surf,temp1i,temp2i,temp3i)
		Next
		
		If isAlpha=1 Then
			AddMesh childMesh,Alpha
		Else
			AddMesh childMesh,Opaque
			EntityParent childMesh,collisionMeshes
			EntityAlpha childMesh,0.0
			EntityType childMesh,HIT_MAP
			EntityPickMode childMesh,2
			
			;make collision double-sided
			Local flipChild% = CopyMesh(childMesh)
			FlipMesh(flipChild)
			AddMesh flipChild,childMesh
			FreeEntity flipChild			
		EndIf
		
		
	Next
	
	Local hiddenMesh%
	hiddenMesh=CreateMesh()
	
	count=ReadInt(f) ;invisible collision mesh
	For i%=1 To count
		surf=CreateSurface(hiddenMesh)
		count2=ReadInt(f) ;vertices
		For j%=1 To count2
			;world coords
			x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
			vertex=AddVertex(surf,x,y,z)
		Next
		
		count2=ReadInt(f) ;polys
		For j%=1 To count2
			temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
			AddTriangle(surf,temp1i,temp2i,temp3i)
			AddTriangle(surf,temp1i,temp3i,temp2i)
		Next
	Next
	
	;trigger boxes
	If hasTriggerBox
		DebugLog "TriggerBoxEnable"
		rt\TempTriggerboxAmount = ReadInt(f)
		For tb = 0 To rt\TempTriggerboxAmount-1
			rt\TempTriggerbox[tb] = CreateMesh(rt\obj)
			count = ReadInt(f)
			For i%=1 To count
				surf=CreateSurface(rt\TempTriggerbox[tb])
				count2=ReadInt(f)
				For j%=1 To count2
					x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
					vertex=AddVertex(surf,x,y,z)
				Next
				count2=ReadInt(f)
				For j%=1 To count2
					temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
					AddTriangle(surf,temp1i,temp2i,temp3i)
					AddTriangle(surf,temp1i,temp3i,temp2i)
				Next
			Next
			rt\TempTriggerboxName[tb] = ReadString(f)
		Next
	EndIf
	
	count=ReadInt(f) ;point entities
	For i%=1 To count
		temp1s=ReadString(f)
		Select temp1s
			Case "screen"
				
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				
				temp2s$ =ReadString(f)
				
				If temp1<>0 Or temp2<>0 Or temp3<>0 Then 
					Local ts.TempScreens = New TempScreens	
					ts\x = temp1
					ts\y = temp2
					ts\z = temp3
					ts\imgpath = temp2s
					ts\roomtemplate = rt
				EndIf
				
			Case "waypoint"
				
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				
				Local w.TempWayPoints = New TempWayPoints
				w\roomtemplate = rt
				w\x = temp1
				w\y = temp2
				w\z = temp3
				
			Case "light"
				
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				
				If temp1<>0 Or temp2<>0 Or temp3<>0 Then 
					range# = ReadFloat(f)/2000.0
					lcolor$=ReadString(f)
					intensity# = Min(ReadFloat(f)*0.8,1.0)
					r%=Int(Piece(lcolor,1," "))*intensity
					g%=Int(Piece(lcolor,2," "))*intensity
					b%=Int(Piece(lcolor,3," "))*intensity
					
					AddTempLight(rt, temp1,temp2,temp3, 2, range, r,g,b)
				Else
					ReadFloat(f) : ReadString(f) : ReadFloat(f)
				EndIf
				
			Case "spotlight"
				
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				
				If temp1<>0 Or temp2<>0 Or temp3<>0 Then 
					range# = ReadFloat(f)/2000.0
					lcolor$=ReadString(f)
					intensity# = Min(ReadFloat(f)*0.8,1.0)
					r%=Int(Piece(lcolor,1," "))*intensity
					g%=Int(Piece(lcolor,2," "))*intensity
					b%=Int(Piece(lcolor,3," "))*intensity
					
					Local lt.LightTemplates = AddTempLight(rt, temp1,temp2,temp3, 2, range, r,g,b)
					angles$=ReadString(f)
					pitch#=Piece(angles,1," ")
					yaw#=Piece(angles,2," ")
					lt\pitch = pitch
					lt\yaw = yaw
					
					lt\innerconeangle = ReadInt(f)
					lt\outerconeangle = ReadInt(f)
				Else
					ReadFloat(f) : ReadString(f) : ReadFloat(f) : ReadString(f) : ReadInt(f) : ReadInt(f)
				EndIf
				
			Case "soundemitter"
				
				temp1i=0
				
				For j = 0 To MaxRoomEmitters-1
					If rt\TempSoundEmitter[j]=0 Then
						rt\TempSoundEmitterX[j]=ReadFloat(f)*RoomScale
						rt\TempSoundEmitterY[j]=ReadFloat(f)*RoomScale
						rt\TempSoundEmitterZ[j]=ReadFloat(f)*RoomScale
						rt\TempSoundEmitter[j]=ReadInt(f)
						
						rt\TempSoundEmitterRange[j]=ReadFloat(f)
						temp1i=1
						Exit
					EndIf
				Next
				
				If temp1i=0 Then
					ReadFloat(f)
					ReadFloat(f)
					ReadFloat(f)
					ReadInt(f)
					ReadFloat(f)
				EndIf
				
			Case "playerstart"
				
				temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
				
				angles$=ReadString(f)
				pitch#=Piece(angles,1," ")
				yaw#=Piece(angles,2," ")
				roll#=Piece(angles,3," ")
				If cam Then
					PositionEntity cam,temp1,temp2,temp3
					RotateEntity cam,pitch,yaw,roll
				EndIf
			Case "model"
				file = ReadString(f)
				If file<>""
					Local model = CreatePropObj("GFX\Map\Props\"+file);LoadMesh("GFX\Map\Props\"+file)
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
					PositionEntity model,temp1,temp2,temp3
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
					RotateEntity model,temp1,temp2,temp3
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
					ScaleEntity model,temp1,temp2,temp3
					
					EntityParent model,Opaque
					EntityType model,HIT_MAP
					EntityPickMode model,2
				Else
					DebugLog "file = 0"
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
					DebugLog temp1+", "+temp2+", "+temp3
					
					;Stop
				EndIf
		End Select
	Next
	
	Local obj%
	
	temp1i=CopyMesh(Alpha)
	FlipMesh temp1i
	AddMesh temp1i,Alpha
	FreeEntity temp1i
	
	If brush <> 0 Then FreeBrush brush
	
	AddMesh Alpha,Opaque
	FreeEntity Alpha
	
	EntityFX Opaque,3
	
	EntityAlpha hiddenMesh,0.0
	EntityAlpha Opaque,1.0
	
	;EntityType Opaque,HIT_MAP
	EntityType hiddenMesh,HIT_MAP
	FreeTexture blankTexture
	
	;AddMesh hiddenMesh,BigRoomMesh
	
	obj=CreatePivot()
	CreatePivot(obj) ;skip "meshes" object
	EntityParent Opaque,obj
	EntityParent hiddenMesh,obj
	CreatePivot(obj) ;skip "pointentites" object
	CreatePivot(obj) ;skip "solidentites" object
	EntityParent collisionMeshes,obj
	
	CloseFile f
	
	Return obj%
	
End Function


;-----------;;;;

Function StripPath$(file$) 
	Local name$=""
	If Len(file$)>0 
		For i=Len(file$) To 1 Step -1 
			
			mi$=Mid$(file$,i,1) 
			If mi$="\" Or mi$="/" Then Return name$
			
			name$=mi$+name$ 
		Next 
		
	EndIf 
	
	Return name$ 
End Function

Function Piece$(s$,entry,char$=" ")
	While Instr(s,char+char)
		s=Replace(s,char+char,char)
	Wend
	For n=1 To entry-1
		p=Instr(s,char)
		s=Right(s,Len(s)-p)
	Next
	p=Instr(s,char)
	If p<1
		a$=s
	Else
		a=Left(s,p-1)
	EndIf
	Return a
End Function

Function KeyValue$(entity,key$,defaultvalue$="")
	properties$=EntityName(entity)
	properties$=Replace(properties$,Chr(13),"")
	key$=Lower(key)
	Repeat
		p=Instr(properties,Chr(10))
		If p Then test$=(Left(properties,p-1)) Else test=properties
		testkey$=Piece(test,1,"=")
		testkey=Trim(testkey)
		testkey=Replace(testkey,Chr(34),"")
		testkey=Lower(testkey)
		If testkey=key Then
			value$=Piece(test,2,"=")
			value$=Trim(value$)
			value$=Replace(value$,Chr(34),"")
			Return value
		EndIf
		If Not p Then Return defaultvalue$
		properties=Right(properties,Len(properties)-p)
	Forever 
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
Const center = 5 ;(gridsize-1) / 2

Type Forest
	Field TileMesh%[6]
	Field DetailMesh%[6]
	Field TileTexture%[10]
	Field grid%[(gridsize*gridsize)+11]
	Field TileEntities%[(gridsize*gridsize)+1]
	Field Forest_Pivot%
	
	Field Door%[2]
	Field DetailEntities%[2]
	
	Field ID%
End Type

Function move_forward%(dir%,pathx%,pathy%,retval%=0)
	;move 1 unit along the grid in the designated direction
	If dir = 1 Then
		If retval=0 Then
			Return pathx
		Else
			Return pathy+1
		EndIf
	EndIf
	If retval=0 Then
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
	If retval=0 Then Return dir Else Return deviated
End Function

Function GenForestGrid(fr.Forest)
	fr\ID=LastForestID+1
	LastForestID=LastForestID+1
	
	Local door1_pos%,door2_pos%
	Local i%,j%
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
	Local pathx = door2_pos
	Local pathy = 1
	Local dir = 1 ;0 = left, 1 = up, 2 = right
	fr\grid[((gridsize-1-pathy)*gridsize)+pathx] = 1
	
	Local deviated%
	
	While pathy < gridsize -4
		If dir = 1 Then ;determine whether to go forward or to the side
			If chance(deviation_chance) Then
				;pick a branch direction
				dir = 2 * Rand(0,1)
				;make sure you have not passed max side distance
				dir = turn_if_deviating(max_deviation_distance,pathx,center,dir)
				deviated = turn_if_deviating(max_deviation_distance,pathx,center,dir,1)
				If deviated Then fr\grid[((gridsize-1-pathy)*gridsize)+pathx]=1
				pathx=move_forward(dir,pathx,pathy)
				pathy=move_forward(dir,pathx,pathy,1)
			EndIf
			
		Else
			;we are going to the side, so determine whether to keep going or go forward again
			dir = turn_if_deviating(max_deviation_distance,pathx,center,dir)
			deviated = turn_if_deviating(max_deviation_distance,pathx,center,dir,1)
			If deviated Or chance(return_chance) Then dir = 1
			
			pathx=move_forward(dir,pathx,pathy)
			pathy=move_forward(dir,pathx,pathy,1)
			;if we just started going forward go twice so as to avoid creating a potential 2x2 line
			If dir=1 Then
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
	
	If pathx<>door1_pos Then
		dir=0
		If door1_pos>pathx Then dir=2
		While pathx<>door1_pos
			pathx=move_forward(dir,pathx,pathy)
			pathy=move_forward(dir,pathx,pathy,1)
			fr\grid[((gridsize-1-pathy)*gridsize)+pathx]=1
		Wend
	EndIf
	
	;attempt to create new branches
	Local new_y%,temp_y%,new_x%
	Local branch_type%,branch_pos%
	new_y=-3 ;used for counting off; branches will only be considered once every 4 units so as to avoid potentially too many branches
	While new_y<gridsize-6
		new_y=new_y+4
		temp_y=new_y
		new_x=0
		If chance(branch_chance) Then
			branch_type=-1
			If chance(cobble_chance) Then
				branch_type=-2
			EndIf
			;create a branch at this spot
			;determine if on left or on right
			branch_pos=2*Rand(0,1)
			;get leftmost or rightmost path in this row
			leftmost=gridsize
			rightmost=0
			For i=0 To gridsize
				If fr\grid[((gridsize-1-new_y)*gridsize)+i]=1 Then
					If i<leftmost Then leftmost=i
					If i>rightmost Then rightmost=i
				EndIf
			Next
			If branch_pos=0 Then new_x=leftmost-1 Else new_x=rightmost+1
			;before creating a branch make sure there are no 1's above or below
			If (temp_y<>0 And fr\grid[((gridsize-1-temp_y+1)*gridsize)+new_x]=1) Or fr\grid[((gridsize-1-temp_y-1)*gridsize)+new_x]=1 Then
				Exit ;break simply to stop creating the branch
			EndIf
			fr\grid[((gridsize-1-temp_y)*gridsize)+new_x]=branch_type ;make 4s so you don't confuse your branch for a path; will be changed later
			If branch_pos=0 Then new_x=leftmost-2 Else new_x=rightmost+2
			fr\grid[((gridsize-1-temp_y)*gridsize)+new_x]=branch_type ;branch out twice to avoid creating an unwanted 2x2 path with the real path
			i = 2
			While i<branch_max_life
				i=i+1
				If chance(branch_die_chance) Then
					Exit
				EndIf
				If Rand(0,3)=0 Then ;have a higher chance to go up to confuse the player
					If branch_pos = 0 Then
						new_x=new_x-1
					Else
						new_x=new_x+1
					EndIf
				Else
					temp_y=temp_y+1
				EndIf
				
				;before creating a branch make sure there are no 1's above or below
				n=((gridsize - 1 - temp_y + 1)*gridsize)+new_x
				If n < gridsize-1 Then 
					If temp_y <> 0 And fr\grid[n]=1 Then Exit
				EndIf
				n=((gridsize - 1 - temp_y - 1)*gridsize)+new_x
				If n>0 Then 
					If fr\grid[n]=1 Then Exit
				EndIf
				
				;If (temp_y <> 0 And fr\grid[((gridsize - 1 - temp_y + 1)*gridsize)+new_x]=1) Or fr\grid[((gridsize - 1 - temp_y - 1)*gridsize)+new_x] = 1 Then
				;	Exit
				;EndIf
				fr\grid[((gridsize-1-temp_y)*gridsize)+new_x]=branch_type ;make 4s so you don't confuse your branch for a path; will be changed later
				If temp_y>=gridsize-2 Then Exit
			Wend
		EndIf
	Wend
	
	;change branches from 4s to 1s (they were 4s so that they didn't accidently create a 2x2 path unintentionally)
	For i=0 To gridsize-1
		For j=0 To gridsize-1
			If fr\grid[(i*gridsize)+j]=-1 Then
				fr\grid[(i*gridsize)+j]=1
			ElseIf fr\grid[(i*gridsize)+j]=-2
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
	Local i%
	
	If fr\Forest_Pivot<>0 Then FreeEntity fr\Forest_Pivot : fr\Forest_Pivot=0
	For i%=0 To 3
		If fr\TileMesh[i]<>0 Then FreeEntity fr\TileMesh[i] : fr\TileMesh[i]=0
	Next
	For i%=0 To 4
		If fr\DetailMesh[i]<>0 Then FreeEntity fr\DetailMesh[i] : fr\DetailMesh[i]=0
	Next
	For i%=0 To 9
		If fr\TileTexture[i]<>0 Then FreeEntity fr\TileTexture[i] : fr\TileTexture[i]=0
	Next
	
	fr\Forest_Pivot=CreatePivot()
	PositionEntity fr\Forest_Pivot,x,y,z,True
	
	;load assets
	
	Local hmap[ROOM4], mask[ROOM4]
	Local GroundTexture = LoadTexture_Strict("GFX\map\forest\forestfloor.jpg")
	;TextureBlend GroundTexture, FE_ALPHACURRENT
	Local PathTexture = LoadTexture_Strict("GFX\map\forest\forestpath.jpg")
	;TextureBlend PathTexture, FE_ALPHACURRENT
	
	hmap[ROOM1]=LoadImage_Strict("GFX\map\forest\forest1h.png")
	mask[ROOM1]=LoadTexture_Strict("GFX\map\forest\forest1h_mask.png",1+2)
	
	hmap[ROOM2]=LoadImage_Strict("GFX\map\forest\forest2h.png")
	mask[ROOM2]=LoadTexture_Strict("GFX\map\forest\forest2h_mask.png",1+2)
	
	hmap[ROOM2C]=LoadImage_Strict("GFX\map\forest\forest2Ch.png")
	mask[ROOM2C]=LoadTexture_Strict("GFX\map\forest\forest2Ch_mask.png",1+2)
	
	hmap[ROOM3]=LoadImage_Strict("GFX\map\forest\forest3h.png")
	mask[ROOM3]=LoadTexture_Strict("GFX\map\forest\forest3h_mask.png",1+2)
	
	hmap[ROOM4]=LoadImage_Strict("GFX\map\forest\forest4h.png")
	mask[ROOM4]=LoadTexture_Strict("GFX\map\forest\forest4h_mask.png",1+2)
	
	For i = ROOM1 To ROOM4
		;TextureBlend mask[i], FE_ALPHAMODULATE
		
		fr\TileMesh[i]=load_terrain(hmap[i],0.03,GroundTexture,PathTexture,mask[i])
	Next
	
	;detail meshes
	;fr\DetailMesh[0]=LoadMesh_strict("GFX\map\forest\detail\860_1_tree1.b3d")
	;fr\DetailMesh[1]=LoadMesh_strict("GFX\map\forest\detail\860_1_tree1_leaves.b3d")
	fr\DetailMesh[1]=LoadMesh_Strict("GFX\map\forest\detail\treetest4.b3d");1.b3d)
	;EntityParent fr\DetailMesh[1],fr\DetailMesh[0]
	fr\DetailMesh[2]=LoadMesh_Strict("GFX\map\forest\detail\rock.b3d")
	fr\DetailMesh[3]=LoadMesh_Strict("GFX\map\forest\detail\rock2.b3d")
	fr\DetailMesh[4]=LoadMesh_Strict("GFX\map\forest\detail\treetest5.b3d")
	fr\DetailMesh[5]=LoadMesh_Strict("GFX\map\forest\wall.b3d")
	
	For i%=ROOM1 To ROOM4
		HideEntity fr\TileMesh[i]
	Next
	For i%=1 To 5
		HideEntity fr\DetailMesh[i]
	Next
	
	tempf3=MeshWidth(fr\TileMesh[ROOM1])
	tempf1=tile_size/tempf3
	
	For tx%=1 To gridsize-1
		For ty%=1 To gridsize-1
			If fr\grid[(ty*gridsize)+tx]=1 Then 
				
				tile_type = 0
				If tx+1<gridsize Then tile_type = (fr\grid[(ty*gridsize)+tx+1]>0)
				If tx-1=>0 Then tile_type = tile_type+(fr\grid[(ty*gridsize)+tx-1]>0)
				
				If ty+1<gridsize Then tile_type = tile_type+(fr\grid[((ty+1)*gridsize)+tx]>0)
				If ty-1=>0 Then tile_type = tile_type+(fr\grid[((ty-1)*gridsize)+tx]>0)
				
				;fr\grid[(ty*gridsize)+tx]=tile_type
				
				Local angle%=0
				Select tile_type
					Case 1
						tile_entity = CopyEntity(fr\TileMesh[ROOM1])
						
						If fr\grid[((ty+1)*gridsize)+tx]>0 Then
							angle = 180
						ElseIf fr\grid[(ty*gridsize)+tx-1]>0
							angle = 270
						ElseIf fr\grid[(ty*gridsize)+tx+1]>0
							angle = 90
						End If
						
						tile_type = ROOM1
					Case 2
						If fr\grid[((ty-1)*gridsize)+tx]>0 And fr\grid[((ty+1)*gridsize)+tx]>0 Then
							tile_entity = CopyEntity(fr\TileMesh[ROOM2])
							tile_type = ROOM2
						ElseIf fr\grid[(ty*gridsize)+tx+1]>0 And fr\grid[(ty*gridsize)+tx-1]>0
							tile_entity = CopyEntity(fr\TileMesh[ROOM2])
							angle = 90
							tile_type = ROOM2
						Else
							tile_entity = CopyEntity(fr\TileMesh[ROOM2C])
							If fr\grid[(ty*gridsize)+tx-1]>0 And fr\grid[((ty+1)*gridsize)+tx]>0 Then
								angle = 180
							ElseIf fr\grid[(ty*gridsize)+tx+1]>0 And fr\grid[((ty-1)*gridsize)+tx]>0
								
							ElseIf fr\grid[(ty*gridsize)+tx-1]>0 And fr\grid[((ty-1)*gridsize)+tx]>0
								angle = 270
							Else
								angle = 90
							EndIf
							tile_type = ROOM2C
						EndIf
					Case 3
						tile_entity = CopyEntity(fr\TileMesh[ROOM3])
						
						If fr\grid[((ty-1)*gridsize)+tx]=0 Then
							angle = 180
						ElseIf fr\grid[(ty*gridsize)+tx-1]=0
							angle = 90
						ElseIf fr\grid[(ty*gridsize)+tx+1]=0
							angle = 270
						End If
						
						tile_type = ROOM3
					Case 4
						tile_entity = CopyEntity(fr\TileMesh[ROOM4])	
						tile_type = ROOM4
					Default 
						DebugLog "tile_type: "+tile_type
				End Select
				
				If tile_type > 0 Then 
					
					Local itemPlaced[4]
					;2, 5, 8
					Local it.Items = Null
					If (ty Mod 3)=2 And itemPlaced[Floor(ty/3)]=False Then
						itemPlaced[Floor(ty/3)]=True
						it.Items = CreateItem("Log #"+Int(Floor(ty/3)+1), "paper", 0,0.5,0)
						EntityType(it\collider, HIT_ITEM)
						EntityParent(it\collider, tile_entity)
					EndIf
					
					;place trees and other details
					;only placed on spots where the value of the heightmap is above 100
					SetBuffer ImageBuffer(hmap[tile_type])
					width = ImageWidth(hmap[tile_type])
					tempf4# = (tempf3/Float(width))
					For lx = 3 To width-2
						For ly = 3 To width-2
							GetColor lx,width-ly
							
							If ColorRed()>Rand(100,260) Then
								Select Rand(0,7)
									Case 0,1,2,3,4,5,6 ;create a tree
										detail_entity=CopyEntity(fr\DetailMesh[1])
										;EntityType detail_entity,HIT_MAP
										tempf2=Rnd(0.25,0.4)
										
										For i = 0 To 3
											d=CopyEntity(fr\DetailMesh[4])
											;ScaleEntity d,tempf2*1.1,tempf2,tempf2*1.1,True
											RotateEntity d, 0, 90*i+Rnd(-20,20), 0
											EntityParent(d,detail_entity)
											
											EntityFX d, 1;+8
										Next
										
										ScaleEntity detail_entity,tempf2*1.1,tempf2,tempf2*1.1,True
										PositionEntity detail_entity,lx*tempf4-(tempf3/2.0),ColorRed()*0.03-Rnd(3.0,3.2),ly*tempf4-(tempf3/2.0),True
										
										RotateEntity detail_entity,Rnd(-5,5),Rnd(360.0),0.0,True
										
										;EntityAutoFade(detail_entity,4.0,6.0)
									Case 7 ;add a rock
										detail_entity=CopyEntity(fr\DetailMesh[2])
										;EntityType detail_entity,HIT_MAP
										tempf2=Rnd(0.01,0.012)
										;ScaleEntity detail_entity,tempf2,tempf2*Rnd(1.0,2.0),tempf2,True
										
										PositionEntity detail_entity,lx*tempf4-(tempf3/2.0),ColorRed()*0.03-1.3,ly*tempf4-(tempf3/2.0),True
										
										EntityFX detail_entity, 1
										
										RotateEntity detail_entity,0.0,Rnd(360.0),0.0,True
									Case 6 ;add a stump
										detail_entity=CopyEntity(fr\DetailMesh[4])
										;EntityType detail_entity,HIT_MAP
										tempf2=Rnd(0.1,0.12)
										ScaleEntity detail_entity,tempf2,tempf2,tempf2,True
										
										PositionEntity detail_entity,lx*tempf4-(tempf3/2.0),ColorRed()*0.03-1.3,ly*tempf4-(tempf3/2.0),True
								End Select
								
								EntityFX detail_entity, 1
								;PositionEntity detail_entity,Rnd(0.0,tempf3)-(tempf3/2.0),ColorRed()*0.03-0.05,Rnd(0.0,tempf3)-(tempf3/2.0),True
								EntityParent detail_entity,tile_entity
							EndIf
						Next
					Next
					SetBuffer BackBuffer()
					
					TurnEntity tile_entity, 0, angle, 0
					
					PositionEntity tile_entity,x+(tx*tile_size),y,z+(ty*tile_size),True
					
					ScaleEntity tile_entity,tempf1,tempf1,tempf1
					EntityType tile_entity,HIT_MAP
					EntityFX tile_entity,1
					EntityParent tile_entity,fr\Forest_Pivot
					
					If it<>Null Then EntityParent it\collider,0
					
					fr\TileEntities[tx+(ty*gridsize)] = tile_entity
				Else
					DebugLog "INVALID TILE @ ("+tx+", "+ty+ "): "+tile_type
				EndIf
			EndIf
			
		Next
	Next
	
	;place the wall		
	For i = 0 To 1
		ty = ((gridsize-1)*i)
		
		For tx = 1 To gridsize-1
			If fr\grid[(ty*gridsize)+tx]=3 Then
				fr\DetailEntities[i]=CopyEntity(fr\DetailMesh[5])
				ScaleEntity fr\DetailEntities[i],RoomScale,RoomScale,RoomScale
				
				fr\Door[i] = CopyEntity(r\Objects[3])
				PositionEntity fr\Door[i],72*RoomScale,32.0*RoomScale,0,True
				RotateEntity fr\Door[i], 0,180,0
				ScaleEntity fr\Door[i],48*RoomScale,45*RoomScale,48*RoomScale,True
				EntityParent fr\Door[i],fr\DetailEntities[i]
				;SetAnimTime fr\Door[i], 0
				
				frame = CopyEntity(r\Objects[2],fr\Door[i])
				PositionEntity frame,0,32.0*RoomScale,0,True
				ScaleEntity frame,48*RoomScale,45*RoomScale,48*RoomScale,True
				EntityParent frame,fr\DetailEntities[i]
				
				EntityType fr\DetailEntities[i],HIT_MAP
				;EntityParent frame,fr\DetailEntities[i]
				
				PositionEntity fr\DetailEntities[i],x+(tx*tile_size),y,z+(ty*tile_size)+(tile_size/2)-(tile_size*i),True
				RotateEntity fr\DetailEntities[i],0,180*i,0
				
				EntityParent fr\DetailEntities[i],fr\Forest_Pivot
			EndIf		
		Next		
	Next
	
End Function

Function DestroyForest(fr.Forest)
	Local tx%,ty%
	For tx% = 0 To gridsize-1
		For ty% = 0 To gridsize-1
			If fr\TileEntities[tx+(ty*gridsize)]<>0 Then
				FreeEntity fr\TileEntities[tx+(ty*gridsize)]
				fr\TileEntities[tx+(ty*gridsize)] = 0
				fr\grid[tx+(ty*gridsize)] = 0
			EndIf
		Next
	Next
	If fr\Door[0]<>0 Then FreeEntity fr\Door[0] : fr\Door[0] = 0
	If fr\Door[1]<>0 Then FreeEntity fr\Door[1] : fr\Door[0] = 1
	If fr\DetailEntities[0]<>0 Then FreeEntity fr\DetailEntities[0] : fr\DetailEntities[0] = 0
	If fr\DetailEntities[1]<>0 Then FreeEntity fr\DetailEntities[1] : fr\DetailEntities[1] = 0
	
	If fr\Forest_Pivot<>0 Then FreeEntity fr\Forest_Pivot : fr\Forest_Pivot=0
	For i%=0 To 3
		If fr\TileMesh[i]<>0 Then FreeEntity fr\TileMesh[i] : fr\TileMesh[i]=0
	Next
	For i%=0 To 4
		If fr\DetailMesh[i]<>0 Then FreeEntity fr\DetailMesh[i] : fr\DetailMesh[i]=0
	Next
	For i%=0 To 9
		If fr\TileTexture[i]<>0 Then FreeEntity fr\TileTexture[i] : fr\TileTexture[i]=0
	Next
	
	;Delete fr
End Function


Function UpdateForest(fr.Forest,ent%)
	;local variables
	Local tx%,ty%
	If Abs(EntityY(ent,True)-EntityY(fr\Forest_Pivot,True))<12.0 Then
		For tx% = 0 To gridsize-1
			For ty% = 0 To gridsize-1
				If fr\TileEntities[tx+(ty*gridsize)]<>0 Then
					If Abs(EntityX(ent,True)-EntityX(fr\TileEntities[tx+(ty*gridsize)],True))<20.0 Then
						If Abs(EntityZ(ent,True)-EntityZ(fr\TileEntities[tx+(ty*gridsize)],True))<20.0 Then
							ShowEntity fr\TileEntities[tx+(ty*gridsize)]
						Else
							HideEntity fr\TileEntities[tx+(ty*gridsize)]
						EndIf
					Else
						HideEntity fr\TileEntities[tx+(ty*gridsize)]
					EndIf
				EndIf
			Next
		Next
	EndIf
End Function

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Const MaxRoomLights% = 32
Const MaxRoomEmitters% = 8
Const MaxRoomObjects% = 30


Const ROOM1% = 1, ROOM2% = 2, ROOM2C% = 3, ROOM3% = 4, ROOM4% = 5

Global RoomTempID%
Type RoomTemplates
	Field obj%, id%
	Field objPath$
	
	Field zone%[5]
	
	;Field ambience%
	
	Field TempSoundEmitter%[MaxRoomEmitters]
	Field TempSoundEmitterX#[MaxRoomEmitters],TempSoundEmitterY#[MaxRoomEmitters],TempSoundEmitterZ#[MaxRoomEmitters]
	Field TempSoundEmitterRange#[MaxRoomEmitters]
	
	Field Shape%, Name$
	Field Commonness%, Large%
	Field DisableDecals%
	
	Field TempTriggerboxAmount
	Field TempTriggerbox[128]
	Field TempTriggerboxName$[128]
End Type 	

Function CreateRoomTemplate.RoomTemplates(meshpath$)
	Local rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = meshpath
	
	rt\id = RoomTempID
	RoomTempID=RoomTempID+1
	
	Return rt
End Function

Function LoadRoomTemplates(file$)
	Local TemporaryString$, i%
	Local rt.RoomTemplates = Null
	Local StrTemp$ = ""
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			StrTemp = GetINIString(file, TemporaryString, "mesh path")
			
			rt = CreateRoomTemplate(StrTemp)
			rt\Name = Lower(TemporaryString)
			
			StrTemp = Lower(GetINIString(file, TemporaryString, "shape"))
			
			Select StrTemp
				Case "room1", "1"
					rt\Shape = ROOM1
				Case "room2", "2"
					rt\Shape = ROOM2
				Case "room2c", "2c"
					rt\Shape = ROOM2C
				Case "room3", "3"
					rt\Shape = ROOM3
				Case "room4", "4"
					rt\Shape = ROOM4
				Default
			End Select
			
			For i = 0 To 4
				rt\zone[i]= GetINIInt(file, TemporaryString, "zone"+(i+1))
			Next
			
			rt\Commonness = Max(Min(GetINIInt(file, TemporaryString, "commonness"), 100), 0)
			rt\Large = GetINIInt(file, TemporaryString, "large")
			rt\DisableDecals = GetINIInt(file, TemporaryString, "disabledecals")
			
		EndIf
	Wend
	
	i = 1
	Repeat
		StrTemp = GetINIString(file, "room ambience", "ambience"+i)
		If StrTemp = "" Then Exit
		
		RoomAmbience[i]=LoadSound_Strict(StrTemp)
		i=i+1
	Forever
	
	CloseFile f
	
End Function


Function LoadRoomMesh(rt.RoomTemplates)
	
	If Instr(rt\objPath,".rmesh")<>0 Then ;file is roommesh
		rt\obj = LoadRMesh(rt\objPath, rt)
	Else
		RuntimeError Chr(34)+rt\objPath+Chr(34)+" is not RMesh!"
	EndIf
	
	If (Not rt\obj) Then RuntimeError "Failed to load map file "+Chr(34)+mapfile+Chr(34)+"."
	
	HideEntity(rt\obj)
	
End Function

Function LoadRoomMeshes()
	Local temp% = 0
	For rt.RoomTemplates = Each RoomTemplates
		temp=temp+1
	Next	
	
	Local i = 0
	For rt.RoomTemplates = Each RoomTemplates
		If Instr(rt\objpath,".rmesh")<>0 Then ;file is roommesh
			rt\obj = LoadRMesh(rt\objPath, rt)
		Else
			RuntimeError Chr(34)+rt\objPath+Chr(34)+" is not RMesh!"
		EndIf
		If (Not rt\obj) Then RuntimeError "Failed to load map file "+Chr(34)+mapfile+Chr(34)+"."
		
		HideEntity(rt\obj)
		DrawLoading(Int(30 + (15.0 / temp)*i))
		i=i+1
	Next
End Function


LoadRoomTemplates("Data\rooms.ini")

Global RoomScale# = 8.0 / 2048.0
Const ZONEAMOUNT = 3

Global MapWidth% = userOptions\mapWidth
Global MapHeight% = userOptions\mapWidth

Dim MapTemp%(MapWidth, MapHeight)
Dim MapFound%(MapWidth, MapHeight)

Global RoomAmbience%[20]

Global Sky

Global HideDistance# = 15.0

Global SecondaryLightOn# = True
Global RemoteDoorOn = True
Global Contained106 = False

Type Rooms
	Field zone%
	
	Field found%
	
	Field obj%
	Field x#, y#, z#
	Field angle%
	Field RoomTemplate.RoomTemplates
	
	Field dist#
	
	Field SoundCHN%
	
	Field fr.Forest
	
	Field SoundEmitter%[MaxRoomEmitters]
	Field SoundEmitterObj%[MaxRoomEmitters]
	Field SoundEmitterRange#[MaxRoomEmitters]
	Field SoundEmitterCHN%[MaxRoomEmitters]
	
	Field Lights%[MaxRoomLights]
	Field LightIntensity#[MaxRoomLights]
	
	Field LightSprites%[MaxRoomLights]	
	
	Field Objects%[MaxRoomObjects]
	Field Levers%[11]
	Field RoomDoors.Doors[7]
	Field NPC.NPCs[12]
	Field grid.Grids
	
	Field Adjacent.Rooms[4]
	Field AdjDoor.Doors[4]
	
	Field NonFreeAble%[10]
	Field Textures%[10]
	
	Field MaxLights% = 0
	Field LightSpriteHidden%[MaxRoomLights]
	Field LightSpritesPivot%[MaxRoomLights]
	Field LightSprites2%[MaxRoomLights]
	Field LightHidden%[MaxRoomLights]
	Field LightFlicker%[MaxRoomLights]
	Field AlarmRotor%[1]
	Field AlarmRotorLight%[1]
	Field TriggerboxAmount
	Field Triggerbox[128]
	Field TriggerboxName$[128]
	Field MaxWayPointY#
End Type 

Const gridsz%=20
Type Grids
	Field grid%[gridsz*gridsz]
	Field angles%[gridsz*gridsz]
	Field Meshes%[7]
	Field Entities%[gridsz*gridsz]
	Field waypoints.WayPoints[gridsz*gridsz]
End Type

Function UpdateGrid(grid.Grids)
	;local variables
	Local tx%,ty%
	For tx% = 0 To gridsz-1
		For ty% = 0 To gridsz-1
			If grid\Entities[tx+(ty*gridsz)]<>0 Then
				If Abs(EntityY(Collider,True)-EntityY(grid\Entities[tx+(ty*gridsz)],True))>4.0 Then Exit
				If Abs(EntityX(Collider,True)-EntityX(grid\Entities[tx+(ty*gridsz)],True))<HideDistance Then
					If Abs(EntityZ(Collider,True)-EntityZ(grid\Entities[tx+(ty*gridsz)],True))<HideDistance Then
						ShowEntity grid\Entities[tx+(ty*gridsz)]
					Else
						HideEntity grid\Entities[tx+(ty*gridsz)]
					EndIf
				Else
					HideEntity grid\Entities[tx+(ty*gridsz)]
				EndIf
			EndIf
		Next
	Next
End Function

Function CreateRoom.Rooms(zone%, roomshape%, x#, y#, z#, name$ = "")
	Local r.Rooms = New Rooms
	Local rt.RoomTemplates
	
	r\zone = zone
	
	r\x = x : r\y = y : r\z = z
	
	If name <> "" Then
		name = Lower(name)
		For rt.RoomTemplates = Each RoomTemplates
			If rt\Name = name Then
				r\RoomTemplate = rt
				
				If rt\obj=0 Then LoadRoomMesh(rt)
				
				r\obj = CopyEntity(rt\obj)
				ScaleEntity(r\obj, RoomScale, RoomScale, RoomScale)
				EntityType(r\obj, HIT_MAP)
				EntityPickMode(r\obj, 2)
				
				PositionEntity(r\obj, x, y, z)
				FillRoom(r)
				
				Return r
			EndIf
		Next
	EndIf
	
	Local temp% = 0
	For rt.RoomTemplates = Each RoomTemplates
		
		For i = 0 To 4
			If rt\zone[i]=zone Then 
				If rt\Shape = roomshape Then temp=temp+rt\Commonness : Exit
			EndIf
		Next
		
	Next
	
	Local RandomRoom% = Rand(temp)
	temp = 0
	For rt.RoomTemplates = Each RoomTemplates
		For i = 0 To 4
			If rt\zone[i]=zone And rt\Shape = roomshape Then
				temp=temp+rt\Commonness
				If RandomRoom > temp - rt\Commonness And RandomRoom <= temp Then
					r\RoomTemplate = rt
					
					If rt\obj=0 Then LoadRoomMesh(rt)
					
					r\obj = CopyEntity(rt\obj)
					ScaleEntity(r\obj, RoomScale, RoomScale, RoomScale)
					EntityType(r\obj, HIT_MAP)
					EntityPickMode(r\obj, 2)
					
					PositionEntity(r\obj, x, y, z)
					FillRoom(r)
					
					Return r	
				End If
			EndIf
		Next
		
	Next
	
End Function

Include "Rooms/Room860.bb"
Include "Rooms/RoomLockroom.bb"
Include "Rooms/RoomLockroom2.bb"
Include "Rooms/RoomGateA.bb"
Include "Rooms/RoomGateAEntrance.bb"
Include "Rooms/RoomGateB.bb"
Include "Rooms/Room372.bb"
Include "Rooms/Room079.bb"
Include "Rooms/RoomCheckpoint1.bb"
Include "Rooms/RoomCheckpoint2.bb"
Include "Rooms/Room2Pit.bb"
Include "Rooms/Room2Testroom.bb"
Include "Rooms/Room3Tunnel.bb"
Include "Rooms/Room2Toilets.bb"
Include "Rooms/Room2Storage.bb"
Include "Rooms/Room2SRoom.bb"
Include "Rooms/Room2POffices.bb"
Include "Rooms/Room2POffices2.bb"
Include "Rooms/Room2Elevator.bb"
Include "Rooms/Room2Cafeteria.bb"
Include "Rooms/Room2Nuke.bb"
Include "Rooms/Room2Tunnel.bb"
Include "Rooms/Room008.bb"
Include "Rooms/Room035.bb"
Include "Rooms/Room513.bb"
Include "Rooms/Room966.bb"
Include "Rooms/Room3Storage.bb"
Include "Rooms/Room049.bb"
Include "Rooms/Room2_2.bb"
Include "Rooms/Room012.bb"
Include "Rooms/RoomTunnel2.bb"
Include "Rooms/Room2Pipes.bb"
Include "Rooms/Room3Pit.bb"
Include "Rooms/Room2Servers.bb"
Include "Rooms/Room3Servers.bb"
Include "Rooms/Room3Servers2.bb"
Include "Rooms/RoomTestroom.bb"
Include "Rooms/Room2Closets.bb"
Include "Rooms/Room2Offices.bb"
Include "Rooms/Room2Offices2.bb"
Include "Rooms/Room2Offices3.bb"
Include "Rooms/Room173.bb"
Include "Rooms/Room2SCPs.bb"
Include "Rooms/Room205.bb"
Include "Rooms/RoomEndroom.bb"
Include "Rooms/Room895.bb"
Include "Rooms/Room2Tesla.bb"
Include "Rooms/Room2Doors.bb"
Include "Rooms/Room914.bb"
Include "Rooms/RoomIntro.bb"
Include "Rooms/Room2CCont.bb"
Include "Rooms/Room106.bb"
Include "Rooms/Room1123.bb"
Include "Rooms/Room178.bb"
Include "Rooms/RoomPocketDimension.bb"
Include "Rooms/Room3z3.bb"
Include "Rooms/Room2_3.bb"
Include "Rooms/Room1Lifts.bb"
Include "Rooms/Room2Servers2.bb"
Include "Rooms/Room2GW.bb"
Include "Rooms/Room3GW.bb"
Include "Rooms/Room1162.bb"
Include "Rooms/Room2SCPs2.bb"
Include "Rooms/Room3Offices.bb"
Include "Rooms/Room2Offices4.bb"
Include "Rooms/Room2SL.bb"
Include "Rooms/Room2_4.bb"
Include "Rooms/Room3z2.bb"
Include "Rooms/RoomLockroom3.bb"

Function FillRoom(r.Rooms)
	Select r\RoomTemplate\Name
		Case "room860"
			FillRoom860(r)
		Case "lockroom"
			FillRoomLockroom(r)
		Case "lockroom2"
			FillRoomLockroom2(r)
		Case "gatea"
			FillRoomGateA(r)
		Case "gateaentrance"
			FillRoomGateAEntrance(r)
		Case "exit1"
			FillRoomGateB(r)
		Case "roompj"
			FillRoom372(r)
		Case "room079"
			FillRoom079(r)
		Case "checkpoint1"
			FillRoomCheckpoint1(r)
		Case "checkpoint2"
			FillRoomCheckpoint2(r)
		Case "room2pit"
			FillRoom2Pit(r)
		Case "room2testroom2"
			FillRoom2Testroom(r)
		Case "room3tunnel"
			FillRoom3Tunnel(r)
		Case "room2toilets"
			FillRoom2Toilets(r)
		Case "room2storage"
			FillRoom2Storage(r)
		Case "room2sroom"
			FillRoom2SRoom(r)
		Case "room2poffices"
			FillRoom2POffices(r)
		Case "room2poffices2"
			FillRoom2POffices2(r)
		Case "room2elevator"
			FillRoom2Elevator(r)
		Case "room2cafeteria"
			FillRoom2Cafeteria(r)
		Case "room2nuke"
			FillRoom2Nuke(r)
		Case "room2tunnel"
			FillRoom2Tunnel(r)
		Case "008"
			FillRoom008(r)
		Case "room035"
			FillRoom035(r)
		Case "room513"
			FillRoom513(r)
		Case "room966"
			FillRoom966(r)
		Case "room3storage"
			FillRoom3Storage(r)
		Case "room049"
			FillRoom049(r)
		Case "room2_2"
			FillRoom2_2(r)
		Case "room012"
			FillRoom012(r)
		Case "tunnel2"
			FillRoomTunnel2(r)
		Case "room2pipes"
			FillRoom2Pipes(r)
		Case "room3pit"
			FillRoom3Pit(r)
		Case "room2servers"
			FillRoom2Servers(r)
		Case "room3servers"
			FillRoom3Servers(r)
		Case "room3servers2"
			FillRoom3Servers2(r)
		Case "testroom"
			FillRoomTestroom(r)
		Case "room2closets"
			FillRoom2Closets(r)
		Case "room2offices"
			FillRoom2Offices(r)
		Case "room2offices2"
			FillRoom2Offices2(r)
		Case "room2offices3"
			FillRoom2Offices3(r)
		Case "start"
			FillRoom173(r)
		Case "room2scps"
			FillRoom2SCPs(r)
		Case "room205"
			FillRoom205(r)
		Case "endroom"
			FillRoomEndroom(r)
		Case "coffin"
			FillRoom895(r)
		Case "room2tesla","room2tesla_lcz","room2tesla_hcz"
			FillRoom2Tesla(r)
		Case "room2doors"
			FillRoom2Doors(r)
		Case "914"
			FillRoom914(r)
		Case "173"
			FillRoomIntro(r)
		Case "room2ccont"
			FillRoom2CCont(r)
		Case "room106"
			FillRoom106(r)
		Case "room1123"
			FillRoom1123(r)
		Case "room178"
			FillRoom178(r)
		Case "pocketdimension"
			FillRoomPocketDimension(r)
		Case "room3z3"
			FillRoom3z3(r)
		Case "room2_3"
			FillRoom2_3(r)
		Case "room1lifts"
			FillRoom1Lifts(r)
		Case "room2servers2"
			FillRoom2Servers2(r)
		Case "room2gw","room2gw_b"
		    FillRoom2GW(r)
		Case "room3gw"
	        FillRoom3GW(r)
		Case "room1162"
			FillRoom1162(r)
		Case "room2scps2"
			FillRoom2SCPs2(r)
		Case "room3offices"
			FillRoom3Offices(r)
		Case "room2offices4"
			FillRoom2Offices4(r)
		Case "room2sl"
			FillRoom2SL(r)
		Case "room2_4"
			FillRoom2_4(r)
		Case "room3z2"
			FillRoom3z2(r)
		Case "lockroom3"
			FillRoomLockroom3(r)
	End Select
	
	For lt.lighttemplates = Each LightTemplates
		If lt\roomtemplate = r\RoomTemplate Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b)
			If newlt <> 0 Then 
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	
	For ts.tempscreens = Each TempScreens
		If ts\roomtemplate = r\RoomTemplate Then
			CreateScreen(r\x+ts\x, r\y+ts\y, r\z+ts\z, ts\imgpath, r)
		EndIf
	Next
	
	For tw.TempWayPoints = Each TempWayPoints
		If tw\roomtemplate = r\RoomTemplate Then
			CreateWaypoint(r\x+tw\x, r\y+tw\y, r\z+tw\z, Null, r)
		EndIf
	Next
	
	If r\RoomTemplate\TempTriggerboxAmount > 0
		r\TriggerboxAmount = r\RoomTemplate\TempTriggerboxAmount
		For i = 0 To r\TriggerboxAmount-1
			r\Triggerbox[i] = CopyEntity(r\RoomTemplate\TempTriggerbox[i],r\obj)
			EntityAlpha r\Triggerbox[i],0.0
			r\TriggerboxName[i] = r\RoomTemplate\TempTriggerboxName[i]
			DebugLog "Triggerbox found: "+i
			DebugLog "Triggerbox "+i+" name: "+r\TriggerboxName[i]
		Next
	EndIf
	
	For i = 0 To MaxRoomEmitters-1
		If r\RoomTemplate\TempSoundEmitter[i]<>0 Then
			r\SoundEmitterObj[i]=CreatePivot(r\obj)
			PositionEntity r\SoundEmitterObj[i], r\x+r\RoomTemplate\TempSoundEmitterX[i],r\y+r\RoomTemplate\TempSoundEmitterY[i],r\z+r\RoomTemplate\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\obj)
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	
End Function

Function UpdateRooms()
	Local dist#, i%, j%, r.Rooms
	
	Local x#,z#,hide%=True
	
	PlayerZone=Min(Max(GetZone(EntityZ(Collider)/8.0),0),ZONEAMOUNT-1)
	
	TempLightVolume=0
	Local foundNewPlayerRoom% = False
	If PlayerRoom<>Null Then
		If Abs(EntityY(Collider) - EntityY(PlayerRoom\obj)) < 1.5 Then
			x = Abs(PlayerRoom\x-EntityX(Collider,True))
			If x < 4.0 Then
				z = Abs(PlayerRoom\z-EntityZ(Collider,True))
				If z < 4.0 Then
					foundNewPlayerRoom = True
				EndIf
			EndIf
			
			If foundNewPlayerRoom = False Then ;it's likely that an adjacent room is the new player room, check for that
				For i=0 To 3
					If PlayerRoom\Adjacent[i]<>Null Then
						x = Abs(PlayerRoom\Adjacent[i]\x-EntityX(Collider,True))
						If x < 4.0 Then
							z = Abs(PlayerRoom\Adjacent[i]\z-EntityZ(Collider,True))
							If z < 4.0 Then
								foundNewPlayerRoom = True
								PlayerRoom = PlayerRoom\Adjacent[i]
								Exit
							EndIf
						EndIf
					EndIf
				Next
			EndIf
		Else
			foundNewPlayerRoom = True ;PlayerRoom stays the same when you're high up, or deep down
		EndIf
	EndIf
	
	For r.Rooms = Each Rooms
		
		x = Abs(r\x-EntityX(Collider,True))
		z = Abs(r\z-EntityZ(Collider,True))
		r\dist = Max(x,z)
		
		
		If x<16 And z < 16 Then
			For i = 0 To MaxRoomEmitters-1
				If r\SoundEmitter[i]<>0 Then 
					dist# = EntityDistance(r\SoundEmitterObj[i],Collider)
					If dist < r\SoundEmitterRange[i] Then
						r\SoundEmitterCHN[i] = LoopSound2(RoomAmbience[r\SoundEmitter[i]],r\SoundEmitterCHN[i], Camera, r\SoundEmitterObj[i],r\SoundEmitterRange[i])
					EndIf
				EndIf
			Next
			
			If (Not foundNewPlayerRoom) And (PlayerRoom<>r) Then				
				If x < 4.0 Then
					If z < 4.0 Then
						If Abs(EntityY(Collider) - EntityY(r\obj)) < 1.5 Then PlayerRoom = r
						foundNewPlayerRoom = True
					EndIf
				EndIf				
			EndIf
		EndIf
		
		hide = True
		
		If r=PlayerRoom Then hide = False
		If hide Then
			If IsRoomAdjacent(PlayerRoom,r) Then hide = False
		EndIf
		If hide Then
			For i=0 To 3
				If (IsRoomAdjacent(PlayerRoom\Adjacent[i],r)) Then hide=False : Exit
			Next
		EndIf
		
		If hide Then
			HideEntity r\obj
		Else
			ShowEntity r\obj
			For i = 0 To MaxRoomLights-1
				If r\Lights[i] <> 0 Then
					dist = EntityDistance(Collider,r\Lights[i])
					If dist < HideDistance Then
						TempLightVolume = TempLightVolume + r\LightIntensity[i]*r\LightIntensity[i]*((HideDistance-dist)/HideDistance)
						;ShowEntity(r\Lights[i]) 						
					EndIf
				Else
					Exit
				EndIf
			Next
		EndIf
	Next
	
	MapFound(Floor(EntityX(PlayerRoom\obj) / 8.0), Floor(EntityZ(PlayerRoom\obj) / 8.0)) = 1
	PlayerRoom\found = True
	
	TempLightVolume = Max(TempLightVolume / 4.5, 1.0)
	
	If PlayerRoom<>Null Then
		EntityAlpha(GetChild(PlayerRoom\obj,2),1)
		For i=0 To 3
			If PlayerRoom\Adjacent[i]<>Null Then
				x = Abs(EntityX(Collider,True)-EntityX(PlayerRoom\AdjDoor[i]\frameobj,True))
				z = Abs(EntityZ(Collider,True)-EntityZ(PlayerRoom\AdjDoor[i]\frameobj,True))
				If PlayerRoom\AdjDoor[i]\openstate = 0 Then
					EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\obj,2),0)
				;ElseIf Abs(DeltaYaw(Camera,PlayerRoom\Adjacent[i]\obj))>90+(((8.0-Max(x,z))/8.0)*90.0) Then
				;	EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\obj,2),0)
				ElseIf (Not EntityInView(PlayerRoom\AdjDoor[i]\frameobj,Camera))
					EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\obj,2),0)
				Else
					EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\obj,2),1)
				EndIf
				
				For j=0 To 3
					If (PlayerRoom\Adjacent[i]\Adjacent[j]<>Null) Then
						If (PlayerRoom\Adjacent[i]\Adjacent[j]<>PlayerRoom) Then EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\Adjacent[j]\obj,2),0)
					EndIf
				Next
			EndIf
		Next
	EndIf
	
End Function

Function IsRoomAdjacent(this.Rooms,that.Rooms)
	If this=Null Then Return False
	If this=that Then Return True
	For i=0 To 3
		If that=this\Adjacent[i] Then Return True
	Next
	Return False
End Function

;-------------------------------------------------------------------------------------------------------

Global LightVolume#, TempLightVolume#
Function AddLight%(room.Rooms, x#, y#, z#, ltype%, range#, r%, g%, b%)
	Local i
	
	If room<>Null Then
		For i = 0 To MaxRoomLights-1
			If room\Lights[i]=0 Then
				room\Lights[i] = CreateLight(ltype)
				;room\LightDist[i] = range
				LightRange(room\Lights[i],range)
				LightColor(room\Lights[i],r,g,b)
				PositionEntity(room\Lights[i],x,y,z,True)
				EntityParent(room\Lights[i],room\obj)
				
				room\LightIntensity[i] = (r+g+b)/255.0/3.0
				
				room\LightSprites[i]= CreateSprite()
				PositionEntity(room\LightSprites[i], x, y, z)
				ScaleSprite(room\LightSprites[i], 0.13 , 0.13)
				EntityTexture(room\LightSprites[i], LightSpriteTex(0))
				EntityBlend (room\LightSprites[i], 3)
				
				EntityParent(room\LightSprites[i], room\obj)
				
				room\LightSpritesPivot[i] = CreatePivot()
				EntityRadius room\LightSpritesPivot[i],0.05
				PositionEntity(room\LightSpritesPivot[i], x, y, z)
				EntityParent(room\LightSpritesPivot[i], room\obj)
				
				room\LightSprites2[i] = CreateSprite()
				PositionEntity(room\LightSprites2[i], x, y, z)
				ScaleSprite(room\LightSprites2[i], 0.6, 0.6)
				EntityTexture(room\LightSprites2[i], LightSpriteTex(2))
				EntityBlend(room\LightSprites2[i], 3)
				EntityOrder(room\LightSprites2[i], -1)
				EntityColor(room\LightSprites2[i], r%, g%, b%)
				EntityParent(room\LightSprites2[i], room\obj)
				EntityFX(room\LightSprites2[i],1)
				RotateEntity(room\LightSprites2[i],0,0,Rand(360))
				SpriteViewMode(room\LightSprites2[i],1)
				room\LightSpriteHidden%[i] = True
				HideEntity room\LightSprites2[i]
				room\LightFlicker%[i] = Rand(1,10)
				
				HideEntity room\Lights[i]
				
				room\MaxLights% = room\MaxLights% + 1
				
				Return room\Lights[i]
			EndIf
		Next
	Else
		Local light%,sprite%
		light=CreateLight(ltype)
		LightRange(light,range)
		LightColor(light,r,g,b)
		PositionEntity(light,x,y,z,True)
		sprite=CreateSprite()
		PositionEntity(sprite, x, y, z)
		ScaleSprite(sprite, 0.13 , 0.13)
		EntityTexture(sprite, LightSpriteTex(0))
		EntityBlend (sprite, 3)
		Return light
	EndIf
End Function

Type LightTemplates
	Field roomtemplate.RoomTemplates
	Field ltype%
	Field x#, y#, z#
	Field range#
	Field r%, g%, b%
	
	Field pitch#, yaw#
	Field innerconeangle%, outerconeangle#
End Type 

Function AddTempLight.LightTemplates(rt.RoomTemplates, x#, y#, z#, ltype%, range#, r%, g%, b%)
	lt.lighttemplates = New LightTemplates
	lt\roomtemplate = rt
	lt\x = x
	lt\y = y
	lt\z = z
	lt\ltype = ltype
	lt\range = range
	lt\r = r
	lt\g = g
	lt\b = b
	
	Return lt
End Function

;-------------------------------------------------------------------------------------------------------

Type TempWayPoints
	Field x#, y#, z#
	Field roomtemplate.RoomTemplates
End Type 

Type WayPoints
	Field obj
	Field door.Doors
	Field room.Rooms
	Field state%
	;Field tempDist#
	;Field tempSteps%
	Field connected.WayPoints[5]
	Field dist#[5]
	
	Field Fcost#, Gcost#, Hcost#
	
	Field parent.WayPoints
End Type

Function CreateWaypoint.WayPoints(x#,y#,z#,door.Doors, room.Rooms)
	
	w.waypoints = New WayPoints
	
	If 1 Then
		w\obj = CreatePivot()
		PositionEntity w\obj, x,y,z	
	Else
		w\obj = CreateSprite()
		PositionEntity(w\obj, x, y, z)
		ScaleSprite(w\obj, 0.15 , 0.15)
		EntityTexture(w\obj, LightSpriteTex(0))
		EntityBlend (w\obj, 3)	
	EndIf
	
	EntityParent w\obj, room\obj
	
	w\room = room
	w\door=door
	
	Return w
End Function

Function InitWayPoints(loadingstart=45)
	
	Local d.Doors, w.WayPoints, w2.WayPoints, r.Rooms, ClosestRoom.Rooms
	
	Local x#, y#, z#
	
	temper = MilliSecs()
	
	Local dist#, dist2#
	
	For d.Doors = Each Doors
		If d\obj <> 0 Then HideEntity d\obj
		If d\obj2 <> 0 Then HideEntity d\obj2	
		If d\frameobj <> 0 Then HideEntity d\frameobj
		
		If d\room = Null Then 
			ClosestRoom.Rooms = Null
			dist# = 30
			For r.Rooms = Each Rooms
				x# = Abs(EntityX(r\obj,True)-EntityX(d\frameobj,True))
				If x < 20.0 Then
					z# = Abs(EntityZ(r\obj,True)-EntityZ(d\frameobj,True))
					If z < 20.0 Then
						dist2 = x*x+z*z
						If dist2 < dist Then
							ClosestRoom = r
							dist = dist2
						EndIf
					EndIf
				EndIf
			Next
		Else
			ClosestRoom = d\room
		EndIf
		
		If (Not d\DisableWaypoint) Then CreateWaypoint(EntityX(d\frameobj, True), EntityY(d\frameobj, True)+0.18, EntityZ(d\frameobj, True), d, ClosestRoom)
	Next
	
	amount# = 0
	For w.WayPoints = Each WayPoints
		EntityPickMode w\obj, 1, True
		EntityRadius w\obj, 0.2
		amount=amount+1
	Next
	
	
	;pvt = CreatePivot()
	
	number = 0
	iter = 0
	For w.WayPoints = Each WayPoints
		
		number = number + 1
		iter = iter + 1
		If iter = 20 Then 
			DrawLoading(loadingstart+Floor((35.0/amount)*number)) 
			iter = 0
		EndIf
		
		w2.WayPoints = After(w)
		
		Local canCreateWayPoint% = False
		
		While (w2<>Null)
			
			If (w\room=w2\room Or w\door<>Null Or w2\door<>Null)
				
				dist# = EntityDistance(w\obj, w2\obj);;Sqr(x*x+y*y+z*z)
				
				If w\room\MaxWayPointY# = 0.0 Or w2\room\MaxWayPointY# = 0.0
					canCreateWayPoint = True
				Else
					If Abs(EntityY(w\obj)-EntityY(w2\obj))<=w\room\MaxWayPointY
						canCreateWayPoint = True
					EndIf
				EndIf
				
				If dist < 7.0 Then
					If canCreateWayPoint
						If EntityVisible(w\obj, w2\obj) Then;e=w2\obj Then 
							For i = 0 To 4
								If w\connected[i] = Null Then
									w\connected[i] = w2.WayPoints 
									w\dist[i] = dist
									Exit
								EndIf
							Next
							
							For n = 0 To 4
								If w2\connected[n] = Null Then 
									w2\connected[n] = w.WayPoints 
									w2\dist[n] = dist
									Exit
								EndIf					
							Next
						EndIf
					EndIf	
				EndIf
			EndIf
			w2 = After(w2)
		Wend
		
	Next
	
	;FreeEntity pvt	
	
	For d.Doors = Each Doors
		If d\obj <> 0 Then ShowEntity d\obj
		If d\obj2 <> 0 Then ShowEntity d\obj2	
		If d\frameobj <> 0 Then ShowEntity d\frameobj		
	Next
	
	For w.WayPoints = Each WayPoints
		EntityPickMode w\obj, 0, 0
		EntityRadius w\obj, 0
		
		For i = 0 To 4
			If w\connected[i]<>Null Then 
				tline = CreateLine(EntityX(w\obj,True),EntityY(w\obj,True),EntityZ(w\obj,True),EntityX(w\connected[i]\obj,True),EntityY(w\connected[i]\obj,True),EntityZ(w\connected[i]\obj,True))
				EntityColor(tline, 255,0,0)
				EntityParent tline, w\obj
			EndIf
		Next
	Next
	
	DebugLog "InitWaypoints() - "+(MilliSecs2()-temper)
	
End Function

Function RemoveWaypoint(w.WayPoints)
	FreeEntity w\obj
	Delete w
End Function


Dim MapF(MapWidth+1, MapHeight+1), MapG(MapWidth+1, MapHeight+1), MapH(MapWidth+1, MapHeight+1)
Dim MapState(MapWidth+1, MapHeight+1)
Dim MapParent(MapWidth+1, MapHeight+1, 2)
Function FindPath(n.NPCs, x#, y#, z#)
	
	DebugLog "findpath: "+n\NPCtype
	
	Local temp%, dist#, dist2#
	Local xtemp#, ytemp#, ztemp#
	
	Local w.WayPoints, StartPoint.WayPoints, EndPoint.WayPoints   
	
	Local StartX% = Floor(EntityX(n\Collider,True) / 8.0 + 0.5), StartZ% = Floor(EntityZ(n\Collider,True) / 8.0 + 0.5)
       ;If StartX < 0 Or StartX > MapWidth Then Return 2
       ;If StartZ < 0 Or StartZ > MapWidth Then Return 2
	
	Local EndX% = Floor(x / 8.0 + 0.5), EndZ% = Floor(z / 8.0 + 0.5)
       ;If EndX < 0 Or EndX > MapWidth Then Return 2
       ;If EndZ < 0 Or EndZ > MapWidth Then Return 2
	
	Local CurrX, CurrZ
	
       ;pathstatus = 0, ei ole etsitty reitti�
       ;pathstatus = 1, reitti l�ydetty
       ;pathstatus = 2, reitti� ei ole olemassa   
	
	For w.WayPoints = Each WayPoints
		w\state = 0
		w\Fcost = 0
		w\Gcost = 0
		w\Hcost = 0
	Next
	
	n\PathStatus = 0
	n\PathLocation = 0
	For i = 0 To 19
		n\Path[i] = Null
	Next
	
	Local pvt = CreatePivot()
	PositionEntity(pvt, x,y,z, True)   
	
	temp = CreatePivot()
	PositionEntity(temp, EntityX(n\Collider,True), EntityY(n\Collider,True)+0.15, EntityZ(n\Collider,True))
	
	dist = 350.0
	For w.WayPoints = Each WayPoints
		xtemp = EntityX(w\obj,True)-EntityX(temp,True)
          ;If xtemp < 8.0 Then
		ztemp = EntityZ(w\obj,True)-EntityZ(temp,True)
             ;If ztemp < 8.0 Then
		ytemp = EntityY(w\obj,True)-EntityY(temp,True)
                ;If ytemp < 8.0 Then
		dist2# = (xtemp*xtemp)+(ytemp*ytemp)+(ztemp*ztemp)
		If dist2 < dist Then 
			;prefer waypoints that are visible
			If Not EntityVisible(w\obj, temp) Then dist2 = dist2*3
			If dist2 < dist Then 
				dist = dist2
				StartPoint = w
			EndIf
		EndIf
                ;EndIf
             ;EndIf
          ;EndIf
	Next
	DebugLog "DIST: "+dist
	
	FreeEntity temp
	
	If StartPoint = Null Then Return 2
	StartPoint\state = 1      
	
       ;If EndPoint = Null Then
	EndPoint = Null
	dist# = 400.0
	For w.WayPoints = Each WayPoints
		xtemp = EntityX(pvt,True)-EntityX(w\obj,True)
          ;If xtemp =< 8.0 Then
		ztemp = EntityZ(pvt,True)-EntityZ(w\obj,True)
             ;If ztemp =< 8 Then
		ytemp = EntityY(pvt,True)-EntityY(w\obj,True)
		dist2# = (xtemp*xtemp)+(ytemp*ytemp)+(ztemp*ztemp)
		
		If dist2 < dist Then ; And EntityVisible(w\obj, pvt)
			dist = dist2
			EndPoint = w
		EndIf            
             ;EndIf
          ;EndIf
	Next
       ;EndIf
	
	FreeEntity pvt
	
	If EndPoint = StartPoint Then
		If dist < 0.4 Then
			Return 0
		Else
			n\Path[0]=EndPoint
			Return 1               
		EndIf
	EndIf
	If EndPoint = Null Then Return 2
	
       ;aloitus- ja lopetuspisteet l�ydetty, aletaan etsi� reitti�
	
	Repeat
		
		temp% = False
		smallest.WayPoints = Null
		dist# = 10000.0
		For w.WayPoints = Each WayPoints
			If w\state = 1 Then
                temp = True
                If (w\Fcost) < dist Then
					dist = w\Fcost
					smallest = w
                EndIf
			EndIf
		Next
		
		If smallest <> Null Then
			
			w = smallest
			w\state = 2
			
			For i = 0 To 4
                If w\connected[i]<>Null Then
					If w\connected[i]\state < 2 Then
						
						If w\connected[i]\state=1 Then ;open list
							gtemp# = w\Gcost+w\dist[i]
							If n\NPCtype = NPCtypeMTF Then
								If w\connected[i]\door = Null Then gtemp = gtemp + 0.5
							EndIf
							If gtemp < w\connected[i]\Gcost Then ;parempi reitti -> overwrite
								w\connected[i]\Gcost = gtemp
								w\connected[i]\Fcost = w\connected[i]\Gcost + w\connected[i]\Hcost
								w\connected[i]\parent = w
							EndIf
						Else
							w\connected[i]\Hcost# = Abs(EntityX(w\connected[i]\obj,True)-EntityX(EndPoint\obj,True))+Abs(EntityZ(w\connected[i]\obj,True)-EntityZ(EndPoint\obj,True))
							gtemp# = w\Gcost+w\dist[i]
							If n\NPCtype = NPCtypeMTF Then
								If w\connected[i]\door = Null Then gtemp = gtemp + 0.5
							EndIf
							w\connected[i]\Gcost = gtemp
							w\connected[i]\Fcost = w\Gcost+w\Hcost
							w\connected[i]\parent = w
							w\connected[i]\state=1
						EndIf            
					EndIf
					
                EndIf
			Next
		Else ;open listilt� ei l�ytynyt mit��n
			If EndPoint\state > 0 Then
                StartPoint\parent = Null
                EndPoint\state = 2
                Exit
			EndIf
		EndIf
		
		If EndPoint\state > 0 Then
			StartPoint\parent = Null
			EndPoint\state = 2
			Exit
		EndIf
		
	Until temp = False
	
	If EndPoint\state > 0 Then
		
		currpoint.waypoints = EndPoint
		twentiethpoint.waypoints = EndPoint
		
		length = 0
		Repeat
			length = length +1
			currpoint = currpoint\parent
			If length>20 Then
                twentiethpoint = twentiethpoint\parent
			EndIf
		Until currpoint = Null
		
		currpoint.waypoints = EndPoint
		While twentiethpoint<>Null
			length=Min(length-1,19)
             ;DebugLog "LENGTH "+length
			twentiethpoint = twentiethpoint\parent
			n\Path[length] = twentiethpoint
		Wend
		
		Return 1
          ;RuntimeError length
    ;      For i = 0 To (length-1)
    ;         temp =False
    ;         If length < 20 Then
    ;            n\Path[length-1-i] = currpoint.WayPoints
    ;         Else
    ;            If i < 20 Then
    ;               n\Path[20-1-i] = w.WayPoints
    ;            Else
    ;               ;Return 1
    ;            EndIf
    ;         EndIf
    ;         
    ;         If currpoint = StartPoint Then Return 1
    ;         
    ;         If currpoint\parent <> Null Then
    ;            currpoint = currpoint\parent
    ;         Else
    ;            Exit
    ;         EndIf
    ;         
    ;      Next
		
	Else
		
		DebugLog "FUNCTION FindPath() - reitti� ei l�ytynyt"
		Return 2 ;reitti� m��r�np��h�n ei l�ytynyt
		
	EndIf
	
End Function
Function CreateLine(x1#,y1#,z1#, x2#,y2#,z2#, mesh=0)
	
	If mesh = 0 Then 
		mesh=CreateMesh()
		EntityFX(mesh,16)
		surf=CreateSurface(mesh)	
		verts = 0	
		
		AddVertex surf,x1#,y1#,z1#,0,0
	Else
		surf = GetSurface(mesh,1)
		verts = CountVertices(surf)-1
	End If
	
	AddVertex surf,(x1#+x2#)/2,(y1#+y2#)/2,(z1#+z2#)/2,0,0 
	; you could skip creating the above vertex and change the line below to
	; AddTriangle surf,verts,verts+1,verts+0
	; so your line mesh would use less vertices, the drawback is that some videocards (like the matrox g400)
	; aren't able to create a triangle with 2 vertices. so, it's your call :)
	AddVertex surf,x2#,y2#,z2#,1,0
	
	AddTriangle surf,verts,verts+2,verts+1
	
	Return mesh
End Function

;-------------------------------------------------------------------------------------------------------

Global SelectedScreen.Screens
Type Screens
	Field obj%
	Field imgpath$
	Field img
	Field room.Rooms
End Type

Type TempScreens
	Field imgpath$
	Field x#,y#,z#
	Field roomtemplate.RoomTemplates
End Type

Function CreateScreen.Screens(x#,y#,z#,imgpath$,r.Rooms)
	s.screens = New Screens
	s\obj = CreatePivot()
	EntityPickMode(s\obj, 1)	
	EntityRadius s\obj, 0.1
	
	PositionEntity s\obj, x,y,z
	s\imgpath = imgpath
	s\room = r
	EntityParent s\obj, r\obj
	
	Return s
End Function

Function UpdateScreens()
	If SelectedScreen <> Null Then Return
	If SelectedDoor <> Null Then Return
	
	For s.screens = Each Screens
		If s\room = PlayerRoom Then
			If EntityDistance(Collider,s\obj)<1.2 Then
				EntityPick(Camera, 1.2)
				If PickedEntity()=s\obj And s\imgpath<>"" Then
					DrawHandIcon=True
					If MouseUp1 Then 
						SelectedScreen=s
						s\img = LoadImage_Strict("GFX\screens\"+s\imgpath)
						s\img = ResizeImage2(s\img, ImageWidth(s\img) * MenuScale, ImageHeight(s\img) * MenuScale)
						MaskImage s\img, 255,0,255
						PlaySound_Strict ButtonSFX
						MouseUp1=False
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
End Function

Dim MapName$(MapWidth, MapHeight)
Dim MapRoomID%(ROOM4 + 1)
Dim MapRoom$(ROOM4 + 1, 0)

;-------------------------------------------------------------------------------------------------------


Dim GorePics%(10)
Global SelectedMonitor.SecurityCams
Global CoffinCam.SecurityCams
Type SecurityCams
	Field obj%, MonitorObj%
	
	Field BaseObj%, CameraObj%
	
	Field ScrObj%, ScrWidth#, ScrHeight#
	Field Screen%, Cam%, ScrTexture%, ScrOverlay%
	Field angle#, turn#, CurrAngle#
	Field State#, PlayerState%
	
	Field soundCHN%
	
	Field InSight%
	
	Field RenderInterval#
	
	Field room.Rooms
	
	Field FollowPlayer%
	Field CoffinEffect%
	
	Field AllowSaving%
	
	Field MinAngle#, MaxAngle#, dir%
	
	Field IsRoom2slCam% = False
	Field Room2slTexs%[2]
	Field SpecialCam% = False
	Field ID% = -1
End Type

Global ScreenTexs%[2]

Function CreateSecurityCam.SecurityCams(x#, y#, z#, r.Rooms, screen% = False)
	Local sc.SecurityCams = New SecurityCams
	
	sc\obj = CopyEntity(CamBaseOBJ)
	ScaleEntity(sc\obj, 0.0015, 0.0015, 0.0015)
	sc\CameraObj = CopyEntity(CamOBJ)
	ScaleEntity(sc\CameraObj, 0.01, 0.01, 0.01)
	
	sc\room = r
	
	sc\Screen = screen
	If screen Then
		sc\AllowSaving = True
		
		sc\RenderInterval = 12
		
		Local scale# = RoomScale * 4.5 * 0.4
		
		sc\ScrObj = CreateSprite()
		EntityFX sc\ScrObj, 17
		SpriteViewMode(sc\ScrObj, 2)
		sc\ScrTexture = 0
		EntityTexture sc\ScrObj, ScreenTexs[sc\ScrTexture]
		ScaleSprite(sc\ScrObj, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
		
		sc\ScrOverlay = CreateSprite(sc\ScrObj)
		;	scaleSprite(sc\scrOverlay , 0.5, 0.4)
		ScaleSprite(sc\ScrOverlay, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
		MoveEntity(sc\ScrOverlay, 0, 0, -0.0005)
		EntityTexture(sc\ScrOverlay, MonitorTexture)
		SpriteViewMode(sc\ScrOverlay, 2)
		EntityBlend(sc\ScrOverlay , 3)
		
		sc\MonitorObj = CopyEntity(Monitor, sc\ScrObj)
		
		ScaleEntity(sc\MonitorObj, scale, scale, scale)
		
		sc\Cam = CreateCamera()
		CameraViewport(sc\Cam, 0, 0, 512, 512)
		CameraRange sc\Cam, 0.05, 6.0
		CameraZoom(sc\Cam, 0.8)
		HideEntity(sc\Cam)	
	End If
	
	PositionEntity(sc\obj, x, y, z)
	
	If r<>Null Then EntityParent(sc\obj, r\obj)
	
	Return sc
End Function

Function UpdateSecurityCams()
	Local sc.SecurityCams
	
	PlayerDetected = False
	
	;coffineffect = 0, not affected by 895
	;coffineffect = 1, constantly affected by 895
	;coffineffect = 2, 079 can broadcast 895 feed on this screen
	;coffineffect = 3, 079 broadcasting 895 feed
	
	For sc.SecurityCams = Each SecurityCams
		Local close = False
		If sc\room = Null And (Not sc\SpecialCam) Then
			HideEntity sc\Cam
		Else
			If (Not sc\SpecialCam)
				If sc\room\dist < 6.0 Or PlayerRoom=sc\room Then 
					close = True
				ElseIf sc\IsRoom2slCam
					close = True
				ElseIf sc\Cam<>0
					HideEntity sc\Cam
				EndIf
			EndIf
			
			If sc\IsRoom2slCam Then sc\CoffinEffect = 0
			If sc\room <> Null
				If sc\room\RoomTemplate\Name$ = "room2sl" Then sc\CoffinEffect = 0
			EndIf
			If sc\SpecialCam Then sc\CoffinEffect = 0
			
			If close Or sc=CoffinCam Or sc\IsRoom2slCam Then 
				If sc\FollowPlayer Then
					If sc<>CoffinCam
						If EntityVisible(sc\CameraObj,Camera)
							PlayerDetected = True
						EndIf
					EndIf
					PointEntity(sc\CameraObj, Camera)
					Local temp# = EntityPitch(sc\CameraObj)
					RotateEntity(sc\obj, 0, CurveAngle(EntityYaw(sc\CameraObj), EntityYaw(sc\obj), 75.0), 0)
					
					If temp < 40.0 Then temp = 40
					If temp > 70.0 Then temp = 70
					RotateEntity(sc\CameraObj, CurveAngle(temp, EntityPitch(sc\CameraObj), 75.0), EntityYaw(sc\obj), 0)
					
					PositionEntity(sc\CameraObj, EntityX(sc\obj, True), EntityY(sc\obj, True) - 0.083, EntityZ(sc\obj, True))
					RotateEntity(sc\CameraObj, EntityPitch(sc\CameraObj), EntityYaw(sc\obj), 0)
				Else
					If sc\turn > 0 Then
						If sc\dir = 0 Then
							sc\CurrAngle=sc\CurrAngle+0.2 * FPSfactor
							If sc\CurrAngle > (sc\turn * 1.3) Then sc\dir = 1
						Else
							sc\CurrAngle=sc\CurrAngle-0.2 * FPSfactor
							If sc\CurrAngle < (-sc\turn * 1.3) Then sc\dir = 0
						End If
					End If
					RotateEntity(sc\obj, 0, sc\room\angle + sc\angle + Max(Min(sc\CurrAngle, sc\turn), -sc\turn), 0)
					
					PositionEntity(sc\CameraObj, EntityX(sc\obj, True), EntityY(sc\obj, True) - 0.083, EntityZ(sc\obj, True))
					RotateEntity(sc\CameraObj, EntityPitch(sc\CameraObj), EntityYaw(sc\obj), 0)
					
					If sc\Cam<>0 Then 
						PositionEntity(sc\Cam, EntityX(sc\CameraObj, True), EntityY(sc\CameraObj, True), EntityZ(sc\CameraObj, True))
						RotateEntity(sc\Cam, EntityPitch(sc\CameraObj), EntityYaw(sc\CameraObj), 0)
						MoveEntity(sc\Cam, 0, 0, 0.1)
					EndIf
					
					If sc<>CoffinCam
						If (Abs(DeltaYaw(sc\CameraObj,Camera))<60.0)
							If EntityVisible(sc\CameraObj,Camera)
								PlayerDetected = True
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			
			If close = True Or sc\IsRoom2slCam Or sc\SpecialCam Then
				If sc\Screen Then 
					sc\State = sc\State+FPSfactor
					
					If sc\InSight And sc\AllowSaving Then 
						If SelectedDifficulty\saveType = SAVEONSCREENS And EntityDistance(Camera, sc\ScrObj)<1.0 Then
							DrawHandIcon = True
							If MouseHit1 Then SelectedMonitor = sc
						Else If SelectedMonitor = sc
							SelectedMonitor = Null
						EndIf
					Else
						SelectedMonitor = Null
					EndIf
					
					If sc\State >= sc\RenderInterval Then
						sc\InSight = False
						If BlinkTimer > - 5 And EntityInView(sc\ScrObj, Camera) Then
							If EntityVisible(Camera,sc\ScrObj) Then
								sc\InSight = True
								
								If sc\CoffinEffect=1 Or sc\CoffinEffect=3 Then
									If BlinkTimer > - 5 Then Sanity=Sanity-(FPSfactor * 16)
									
									If Sanity < (-1000) Then 
										DeathMSG = Chr(34)+"What we know is that he died of cardiac arrest. My guess is that it was caused by SCP-895, although it has never been observed affecting video equipment from this far before. "
										DeathMSG = DeathMSG + "Further testing is needed to determine whether SCP-895's "+Chr(34)+"Red Zone"+Chr(34)+" is increasing."+Chr(34)
										
										Kill()				
									EndIf
								End If
								
								If (Not sc\IsRoom2slCam)
									If (Not sc\SpecialCam)
										If CoffinCam = Null Or Rand(5)=5 Or sc\CoffinEffect <> 3 Then
											HideEntity(Camera)
											ShowEntity(sc\Cam)
											Cls
											
											UpdateRoomLights(sc\Cam)
											
											SetBuffer BackBuffer()
											RenderWorld
											CopyRect 0,0,512,512,0,0,BackBuffer(),TextureBuffer(ScreenTexs[sc\ScrTexture])
											
											HideEntity(sc\Cam)
											ShowEntity(Camera)										
										Else
											HideEntity(Camera)
											ShowEntity (CoffinCam\room\obj)
											EntityAlpha(GetChild(CoffinCam\room\obj,2),1)
											ShowEntity(CoffinCam\Cam)
											Cls
											
											UpdateRoomLights(CoffinCam\Cam)
											
											SetBuffer BackBuffer()
											RenderWorld
											CopyRect 0,0,512,512,0,0,BackBuffer(),TextureBuffer(ScreenTexs[sc\ScrTexture])
											
											HideEntity (CoffinCam\room\obj)
											HideEntity(CoffinCam\Cam)
											ShowEntity(Camera)										
										EndIf
									Else
										HideEntity(Camera)
										ShowEntity(sc\Cam)
										Cls
										
										UpdateRoomLights(sc\Cam)
										
										RenderWorld
										
										HideEntity(sc\Cam)
										ShowEntity(Camera)	
										
										CopyRect(0,0,512,512,0,0,BackBuffer(),TextureBuffer(sc\Room2slTexs[sc\ScrTexture]))
										
									EndIf
								Else
									HideEntity(Camera)
									ShowEntity (sc\room\obj)
									EntityAlpha(GetChild(sc\room\obj,2),1)
									ShowEntity(sc\Cam)
									Cls
									
									UpdateRoomLights(sc\Cam)
									
									RenderWorld
									
									HideEntity (sc\room\obj)
									HideEntity(sc\Cam)
									ShowEntity(Camera)	
									
									CopyRect(0,0,userOptions\screenWidth,userOptions\screenHeight,0,0,BackBuffer(),TextureBuffer(sc\Room2slTexs[sc\ScrTexture]))
								EndIf
								
							EndIf
						EndIf
						sc\State = 0
					End If
					
					If SelectedMonitor = sc Or sc\CoffinEffect=1 Or sc\CoffinEffect=3 Then
						If sc\InSight Then
						;If (Not NoClip) Then 
							Local pvt% = CreatePivot()
							PositionEntity pvt, EntityX(Camera), EntityY(Camera), EntityZ(Camera)
							PointEntity(pvt, sc\ScrObj)
							
							DebugLog("curvea: "+CurveAngle(EntityYaw(pvt), EntityYaw(Collider), Min(Max(15000.0 / (-Sanity), 20.0), 200.0)))
							RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), Min(Max(15000.0 / (-Sanity), 20.0), 200.0)), 0)
							
							TurnEntity(pvt, 90, 0, 0)
							user_camera_pitch = CurveAngle(EntityPitch(pvt), user_camera_pitch + 90.0, Min(Max(15000.0 / (-Sanity), 20.0), 200.0))
							user_camera_pitch=user_camera_pitch-90						
							
							DebugLog("pvt: "+EntityYaw(pvt)+"   - coll: "+EntityYaw(Collider))
							
							
							FreeEntity pvt
						;EndIf
							If sc\CoffinEffect=1 Or sc\CoffinEffect=3 Then
								If Sanity < - 800 Then
									If Rand(3) = 1 Then EntityTexture(sc\ScrOverlay, MonitorTexture)
									If Rand(6) < 5 Then
										EntityTexture(sc\ScrOverlay, GorePics(Rand(0, 5)))
										If sc\PlayerState = 1 Then PlaySound_Strict(HorrorSFX(1))
										sc\PlayerState = 2
										If sc\soundCHN = 0 Then
											sc\soundCHN = PlaySound_Strict(HorrorSFX(4))
										Else
											If Not ChannelPlaying(sc\soundCHN) Then sc\soundCHN = PlaySound_Strict(HorrorSFX(4))
										End If
										If sc\CoffinEffect=3 And Rand(200)=1 Then sc\CoffinEffect=2 : sc\PlayerState = Rand(10000, 20000)
									End If	
									BlurTimer = 1000
								ElseIf Sanity < - 500
									If Rand(7) = 1 Then EntityTexture(sc\ScrOverlay, MonitorTexture)
									If Rand(50) = 1 Then
										EntityTexture(sc\ScrOverlay, GorePics(Rand(0, 5)))
										If sc\PlayerState = 0 Then PlaySound_Strict(HorrorSFX(0))
										sc\PlayerState = Max(sc\PlayerState, 1)
										If sc\CoffinEffect=3 And Rand(100)=1 Then sc\CoffinEffect=2 : sc\PlayerState = Rand(10000, 20000)
									End If
								Else
									EntityTexture(sc\ScrOverlay, MonitorTexture)
								EndIf
							EndIf
						EndIf
					EndIf 
					
					If sc\InSight And sc\CoffinEffect=0 Or sc\CoffinEffect=2 Then
						If sc\PlayerState = 0 Then
							sc\PlayerState = Rand(60000, 65000)
						EndIf
						
						If Rand(500) = 1 Then
							EntityTexture(sc\ScrOverlay, OldAiPics(0))
						End If
						
						If (MilliSecs2() Mod sc\PlayerState) >= Rand(600) Then
							EntityTexture(sc\ScrOverlay, MonitorTexture)
						Else
							If sc\soundCHN = 0 Then
								sc\soundCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\079\Broadcast"+Rand(1,3)+".ogg"))
								If sc\CoffinEffect=2 Then sc\CoffinEffect=3 : sc\PlayerState = 0
							ElseIf (Not ChannelPlaying(sc\soundCHN))
								sc\soundCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\079\Broadcast"+Rand(1,3)+".ogg"))
								If sc\CoffinEffect=2 Then sc\CoffinEffect=3 : sc\PlayerState = 0
							EndIf
							EntityTexture(sc\ScrOverlay, OldAiPics(0))
						EndIf
						
					EndIf
					
				EndIf ;if screen=true
				
				If (Not sc\InSight) Then sc\soundCHN = LoopSound2(CameraSFX, sc\soundCHN, Camera, sc\CameraObj, 4.0)
			Else
				If SelectedMonitor=sc Then SelectedMonitor=Null
			EndIf
		EndIf
	Next
	
	Cls
	
	
End Function


Function UpdateLever(obj, locked=False)
	
	Local dist# = EntityDistance(Camera, obj)
	If dist < 8.0 Then 
		If dist < 0.8 And (Not locked) Then 
			If EntityInView(obj, Camera) Then 
				
				EntityPick(Camera, 0.65)
				
				If PickedEntity() = obj Then
					DrawHandIcon = True
					If MouseHit1 Then GrabbedEntity = obj
				End If
				
				prevpitch# = EntityPitch(obj)
				
				If (MouseDown1 Or MouseHit1) Then
					If GrabbedEntity <> 0 Then
						If GrabbedEntity = obj Then
							DrawHandIcon = True 
							;TurnEntity(obj, , 0, 0)
							RotateEntity(GrabbedEntity, Max(Min(EntityPitch(obj)+Max(Min(mouse_y_speed_1 * 8,30.0),-30), 80), -80), EntityYaw(obj), 0)
							
							DrawArrowIcon(0) = True
							DrawArrowIcon(2) = True
							
						EndIf
					EndIf
				EndIf 
				
				If EntityPitch(obj,True) > 75 Then ;p��ll�
					If prevpitch =< 75 Then PlaySound2(LeverSFX, Camera, obj, 1.0)
				ElseIf EntityPitch(obj,True) < -75 ;pois p��lt�
					If prevpitch => -75 Then PlaySound2(LeverSFX, Camera, obj, 1.0)	
				EndIf						
			EndIf
		EndIf
		
		If MouseDown1=False And MouseHit1=False Then 
			If EntityPitch(obj,True) > 0 Then
				RotateEntity(obj, CurveValue(80, EntityPitch(obj), 10), EntityYaw(obj), 0)
			Else
				RotateEntity(obj, CurveValue(-80, EntityPitch(obj), 10), EntityYaw(obj), 0)
			EndIf
			GrabbedEntity = 0
		End If
		
	EndIf
	
	If EntityPitch(obj,True) > 0 Then ;p��ll�
		Return True
	Else ;pois p��lt�
		Return False
	EndIf	
	
End Function

Function UpdateButton(obj)
	
	Local dist# = EntityDistance(Collider, obj);entityDistance(collider, d\buttons[i])
	If dist < 0.8 Then
		Local temp% = CreatePivot()
		PositionEntity temp, EntityX(Camera), EntityY(Camera), EntityZ(Camera)
		PointEntity temp,obj
		
		If EntityPick(temp, 0.65) = obj Then
			If ClosestButton = 0 Then 
				ClosestButton = obj
			Else
				If dist < EntityDistance(Collider, ClosestButton) Then ClosestButton = obj
			End If							
		End If
		
		FreeEntity temp
	EndIf			
	
End Function

Function UpdateElevators#(State#, door1.Doors, door2.Doors, room1, room2, event.Events)
	Local x#, z#, n.NPCs, NPC_inside.NPCs
	
	door1\IsElevatorDoor = 1
	door2\IsElevatorDoor = 1
	If door1\open = True And door2\open = False Then 
		State = -1
		If (ClosestButton = door2\buttons[0] Or ClosestButton = door2\buttons[1]) And MouseHit1 Then
			UseDoor(door1,False)
		EndIf
		If door2\NPCCalledElevator = True
			UseDoor(door1,False)
			door2\NPCCalledElevator = 2
		EndIf
	ElseIf door2\open = True And door1\open = False
		State = 1
		If (ClosestButton = door1\buttons[0] Or ClosestButton = door1\buttons[1]) And MouseHit1 Then
			UseDoor(door2,False)
		EndIf
		If door1\NPCCalledElevator = True
			UseDoor(door2,False)
			door1\NPCCalledElevator = 2
		EndIf
	ElseIf Abs(door1\openstate-door2\openstate)<0.2 Then
		door1\IsElevatorDoor = 2
		door2\IsElevatorDoor = 2
	EndIf
	
	Local inside = False
	NPC_inside = Null
	
	;molemmat ovet kiinni = hissi liikkuu
	If door1\open = False And door2\open = False Then
		door1\locked = True 
		door2\locked = True
		door1\NPCCalledElevator = 2
		door2\NPCCalledElevator = 2
		If State < 0 Then ;ylh��lt� alas
			State = State - FPSfactor
			;pelaaja hissin sis�ll�
			If Abs(EntityX(Collider)-EntityX(room1,True))<280.0*RoomScale Then
				If Abs(EntityZ(Collider)-EntityZ(room1,True))<280.0*RoomScale Then
					If Abs(EntityY(Collider)-EntityY(room1,True))<280.0*RoomScale Then
						inside = True
						
						If event\SoundCHN = 0 Then
							event\SoundCHN = PlaySound_Strict(ElevatorMoveSFX)
						Else
							If (Not ChannelPlaying(event\SoundCHN)) Then event\SoundCHN = PlaySound_Strict(ElevatorMoveSFX)
						EndIf
						
						CameraShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf
			
			For n.NPCs = Each NPCs
				If n\CanUseElevator
					If Abs(EntityX(n\Collider)-EntityX(room1,True))<280.0*RoomScale
						If Abs(EntityZ(n\Collider)-EntityZ(room1,True))<280.0*RoomScale Then
							If Abs(EntityY(n\Collider)-EntityY(room1,True))<280.0*RoomScale Then
								NPC_inside = n
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If NPC_inside <> Null And (Not inside)
				NPC_inside\Idle = True
			ElseIf NPC_inside <> Null And inside
				NPC_inside\Idle = False
			EndIf
			
			If State < -400 Then
				door1\locked = False
				door2\locked = False
				door1\NPCCalledElevator = False
				door2\NPCCalledElevator = False
				State = 0
				
				If inside Then
					x# = Max(Min((EntityX(Collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min((EntityZ(Collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(Collider, EntityX(room2,True)+x,0.1+EntityY(room2,True)+(EntityY(Collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True)
					ResetEntity Collider	
					UpdateDoorsTimer = 0
					DropSpeed = 0
					UpdateDoors()
					UpdateRooms()
				EndIf
				
				If NPC_inside <> Null
					x# = Max(Min((EntityX(NPC_inside\Collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min((EntityZ(NPC_inside\Collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(NPC_inside\Collider, EntityX(room2,True)+x,0.1+EntityY(room2,True)+(EntityY(NPC_inside\Collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True)
					ResetEntity NPC_inside\Collider	
					UpdateDoorsTimer = 0
					NPC_inside\DropSpeed = 0
					If NPC_inside\Idle
						TurnEntity NPC_inside\obj,0,180,0
						TurnEntity NPC_inside\Collider,0,180,0
						NPC_inside\Idle = False
					EndIf
					NPC_inside\CurrElevator = Null
				EndIf
				
				UseDoor(door2,False)
				
				PlaySound2(ElevatorBeepSFX, Camera, room1, 4.0)
			EndIf
		Else ;alhaalta yl�s
			State = State + FPSfactor
			;pelaaja hissin sis�ll�
			If Abs(EntityX(Collider)-EntityX(room2,True))<280.0*RoomScale Then
				If Abs(EntityZ(Collider)-EntityZ(room2,True))<280.0*RoomScale Then
					If Abs(EntityY(Collider)-EntityY(room2,True))<280.0*RoomScale Then
						inside = True
						
						If event\SoundCHN = 0 Then
							event\SoundCHN = PlaySound_Strict(ElevatorMoveSFX)
						Else
							If (Not ChannelPlaying(event\SoundCHN)) Then event\SoundCHN = PlaySound_Strict(ElevatorMoveSFX)
						EndIf
						
						CameraShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf	
			
			For n.NPCs = Each NPCs
				If n\CanUseElevator
					If Abs(EntityX(n\Collider)-EntityX(room2,True))<280.0*RoomScale
						If Abs(EntityZ(n\Collider)-EntityZ(room2,True))<280.0*RoomScale Then
							If Abs(EntityY(n\Collider)-EntityY(room2,True))<280.0*RoomScale Then
								NPC_inside = n
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If NPC_inside <> Null And (Not inside)
				NPC_inside\Idle = True
			ElseIf NPC_inside <> Null And inside
				NPC_inside\Idle = False
			EndIf
			
			If State > 400 Then 
				door1\locked = False
				door2\locked = False
				door1\NPCCalledElevator = False
				door2\NPCCalledElevator = False
				State = 0
				
				;pelaaja hissin sis�ll�, siirret��n
				If inside Then	
					x# = Max(Min((EntityX(Collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min((EntityZ(Collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(Collider, EntityX(room1,True)+x,0.1+EntityY(room1,True)+(EntityY(Collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True)
					ResetEntity Collider
					UpdateDoorsTimer = 0
					DropSpeed = 0
					UpdateDoors()
					UpdateRooms()
				EndIf
				
				If NPC_inside <> Null
					x# = Max(Min((EntityX(NPC_inside\Collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min((EntityZ(NPC_inside\Collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(NPC_inside\Collider, EntityX(room1,True)+x,0.1+EntityY(room1,True)+(EntityY(NPC_inside\Collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True)
					ResetEntity NPC_inside\Collider
					UpdateDoorsTimer = 0
					NPC_inside\DropSpeed = 0
					If NPC_inside\Idle
						TurnEntity NPC_inside\obj,0,180,0
						TurnEntity NPC_inside\Collider,0,180,0
						NPC_inside\Idle = False
					EndIf
					NPC_inside\CurrElevator = Null
				EndIf
				
				UseDoor(door1,False)
				
				PlaySound2(ElevatorBeepSFX, Camera, room2, 4.0)				
			EndIf	
			
		EndIf
	EndIf
	
	Return State
	
End Function

Function UpdateElevators2#(State#, door1.Doors, door2.Doors, room1, room2, event.Events)
	Local x#, z#
	
	door1\IsElevatorDoor = 1
	door2\IsElevatorDoor = 1
	If door1\open = True And door2\open = False Then 
		State = -1
		door2\IsElevatorDoor = 2
		If (ClosestButton = door2\buttons[0] Or ClosestButton = door2\buttons[1]) And MouseHit1 Then
			UseDoor(door1,False)
		EndIf
	ElseIf door2\open = True And door1\open = False
		State = 1
		door1\IsElevatorDoor = 2
		If (ClosestButton = door1\buttons[0] Or ClosestButton = door1\buttons[1]) And MouseHit1 Then
			UseDoor(door2,False)
		EndIf
	ElseIf Abs(door1\openstate-door2\openstate)<0.2 Then
		door1\IsElevatorDoor = 2
		door2\IsElevatorDoor = 2
	EndIf
	
	Local inside = False
	
	;molemmat ovet kiinni = hissi liikkuu
	If door1\open = False And door2\open = False Then
		door1\locked = True 
		door2\locked = True 
		If State < 0 Then ;ylh��lt?alas
			State = State - FPSfactor
			;pelaaja hissin sis�ll?
			If Abs(EntityX(Collider)-EntityX(room1,True))<280.0*RoomScale Then
				If Abs(EntityZ(Collider)-EntityZ(room1,True))<280.0*RoomScale Then	
					If Abs(EntityY(Collider)-EntityY(room1,True))<280.0*RoomScale Then	
						inside = True
						
						If event\SoundCHN = 0 Then
							event\SoundCHN = PlaySound_Strict(ElevatorMoveSFX)
						Else
							If (Not ChannelPlaying(event\SoundCHN)) Then event\SoundCHN = PlaySound_Strict(ElevatorMoveSFX)
						EndIf
						
						CameraShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf
			
			If State < -400 Then
				door1\locked = False
				door2\locked = False				
				State = 0
				
				UseDoor(door2,True)							
				
				If inside Then
					
					dist# = Distance(EntityX(Collider,True),EntityZ(Collider,True),EntityX(room1,True),EntityZ(room1,True))
					
					dir# = GetAngle(EntityX(Collider,True),EntityZ(Collider,True),EntityX(room1,True),EntityZ(room1,True))
					dir=dir+EntityYaw(room2,True)-EntityYaw(room1,True);EntityYaw(room2,True)+angleDist(dir,EntityYaw(room1,True))
					;dir=dir-90.0
					
;					dir# = EntityYaw(Collider)-EntityYaw(room1,True)+EntityYaw(room2,True)
					
					dir=WrapAngle(dir)
					
					x# = Max(Min(Cos(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min(Sin(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					
					;x# = Max(Min((EntityX(Collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					;z# = Max(Min((EntityZ(Collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					
					RotateEntity Collider,EntityPitch(Collider,True),EntityYaw(room2,True)+angleDist(EntityYaw(Collider,True),EntityYaw(room1,True)),EntityRoll(Collider,True),True ;dir
					
					PositionEntity Collider, EntityX(room2,True)+x,0.05+EntityY(room2,True)+(EntityY(Collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True
					
					ResetEntity Collider	
					UpdateDoors()
					UpdateRooms()
				EndIf
				
				PlaySound2(ElevatorBeepSFX, Camera, room1, 4.0)	
				;PlaySound_Strict(ElevatorBeepSFX)	
			EndIf
		Else ;alhaalta yl�s
			State = State + FPSfactor
			;pelaaja hissin sis�ll?
			If Abs(EntityX(Collider)-EntityX(room2,True))<280.0*RoomScale Then
				If Abs(EntityZ(Collider)-EntityZ(room2,True))<280.0*RoomScale Then	
					If Abs(EntityY(Collider)-EntityY(room2,True))<280.0*RoomScale Then
						inside = True
						
						If event\SoundCHN = 0 Then
							event\SoundCHN = PlaySound_Strict(ElevatorMoveSFX)
						Else
							If (Not ChannelPlaying(event\SoundCHN)) Then event\SoundCHN = PlaySound_Strict(ElevatorMoveSFX)
						EndIf
						
						CameraShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf	
			
			If State > 400 Then 
				door1\locked = False
				door2\locked = False				
				State = 0
				
				UseDoor(door1,True)	
				
				;pelaaja hissin sis�ll? siirret��n
				If inside Then	
					
					dist# = Distance(EntityX(Collider,True),EntityZ(Collider,True),EntityX(room2,True),EntityZ(room2,True))
					
					dir# = GetAngle(EntityX(Collider,True),EntityZ(Collider,True),EntityX(room2,True),EntityZ(room2,True))
					dir=dir+EntityYaw(room1,True)-EntityYaw(room2,True) ;EntityYaw(room1,True)+angleDist(dir,EntityYaw(room2,True))
					;dir=dir-90.0
					
;					dir# = EntityYaw(Collider)-EntityYaw(room2,True)+EntityYaw(room1,True)
					
					;dir=WrapAngle(dir)
					
					x# = Max(Min(Cos(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min(Sin(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					
					;x# = Max(Min((EntityX(Collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					;z# = Max(Min((EntityZ(Collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					
					RotateEntity Collider,EntityPitch(Collider,True),EntityYaw(room2,True)+angleDist(EntityYaw(Collider,True),EntityYaw(room1,True)),EntityRoll(Collider,True),True ;dir
					
					PositionEntity Collider, EntityX(room1,True)+x,0.05+EntityY(room1,True)+(EntityY(Collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True
					
					ResetEntity Collider
					UpdateDoors()
					UpdateRooms()
				EndIf
				
				PlaySound2(ElevatorBeepSFX, Camera, room2, 4.0)				
			EndIf	
			
		EndIf
	EndIf
	
	Return State
	
End Function
;-------------------------------------------------------------------------------------------------------

Type Props
	Field file$
	Field obj
End Type

Function CreatePropObj(file$)
	Local p.Props
	For p.Props = Each Props
		If p\file = file Then
			Return CopyEntity(p\obj)
		EndIf
	Next
	
	p.Props = New Props
	p\file = file
	p\obj = LoadMesh(file)
	Return p\obj
End Function

;-------------------------------------------------------------------------------------------------------

Function CreateMap()
	DebugLog ("Generating a map using the seed "+RandomSeed)
	
	
	Local x%, y%, temp%
	Local i%, x2%, y2%
	Local width%, height%
	
	Local zone%
	
	Local strtemp$ = ""
	For i = 1 To Len(RandomSeed)
		strtemp = strtemp+Asc(Mid(RandomSeed,i,1))
	Next
	SeedRnd Abs(Int(strtemp))
	
	Dim MapName$(MapWidth, MapHeight)
	
	Dim MapRoomID%(ROOM4 + 1)
	
	x = Floor(MapWidth / 2)
	y = MapHeight - 2;Rand(3, 5)
	
	For i = y To MapHeight - 1
		MapTemp(x, i) = True
	Next
	
	Repeat
		width = Rand(10, 15)
		
		If x > MapWidth*0.6 Then
			width = -width
		ElseIf x > MapWidth*0.4
			x = x-width/2
		EndIf
		
		;make sure the hallway doesn't go outside the array
		If x+width > MapWidth-3 Then
			;x = -width+MapWidth-4
			
			width=MapWidth-3-x
		ElseIf x+width < 2
			
			;x = 3-width
			width=-x+2
		EndIf
		
		x = Min(x, x + width)
		width = Abs(width)
		For i = x To x + width
			MapTemp(Min(i,MapWidth), y) = True
		Next
		
		height = Rand(3, 4)
		If y - height < 1 Then height = y-1
		
		yhallways = Rand(4,5)
		
		If GetZone(y-height)<>GetZone(y-height+1) Then height=height-1
		
		For i = 1 To yhallways
			
			x2 = Max(Min(Rand(x, x + width-1),MapWidth-2),2)
			While MapTemp(x2, y - 1) Or MapTemp(x2 - 1, y - 1) Or MapTemp(x2 + 1, y - 1)
				x2=x2+1
			Wend
			
			If x2<x+width Then
				If i = 1 Then
					tempheight = height 
					If Rand(2)=1 Then x2 = x Else x2 = x+width
				Else
					tempheight = Rand(1,height)
				EndIf
				
				For y2 = y - tempheight To y
					If GetZone(y2)<>GetZone(y2+1) Then ;a room leading from zone to another
						MapTemp(x2, y2) = 255
					Else
						MapTemp(x2, y2) = True
					EndIf
				Next
				
				If tempheight = height Then temp = x2
			End If
			
		Next
		
		x = temp
		y = y - height
	Until y < 2
	
	
	Local ZoneAmount=3
	Local Room1Amount%[3], Room2Amount%[3],Room2CAmount%[3],Room3Amount%[3],Room4Amount%[3]
	
	;count the amount of rooms
	For y = 1 To MapHeight - 1
		zone% = GetZone(y)
		
		For x = 1 To MapWidth - 1
			If MapTemp(x, y) > 0 Then
				temp = Min(MapTemp(x + 1, y),1) + Min(MapTemp(x - 1, y),1)
				temp = temp + Min(MapTemp(x, y + 1),1) + Min(MapTemp(x, y - 1),1)			
				If MapTemp(x,y)<255 Then MapTemp(x, y) = temp
				Select MapTemp(x,y)
					Case 1
						Room1Amount[zone]=Room1Amount[zone]+1
					Case 2
						If Min(MapTemp(x + 1, y),1) + Min(MapTemp(x - 1, y),1)= 2 Then
							Room2Amount[zone]=Room2Amount[zone]+1	
						ElseIf Min(MapTemp(x, y + 1),1) + Min(MapTemp(x , y - 1),1)= 2
							Room2Amount[zone]=Room2Amount[zone]+1	
						Else
							Room2CAmount[zone] = Room2CAmount[zone]+1
						EndIf
					Case 3
						Room3Amount[zone]=Room3Amount[zone]+1
					Case 4
						Room4Amount[zone]=Room4Amount[zone]+1
				End Select
			EndIf
		Next
	Next		
	
	;force more room1s (if needed)
	For i = 0 To 2
		;need more rooms if there are less than 5 of them
		temp = -Room1Amount[i]+5
		
		If temp > 0 Then
			
			For y = (MapHeight/ZoneAmount)*(2-i)+1 To ((MapHeight/ZoneAmount) * ((2-i)+1.0))-2
				
				For x = 2 To MapWidth - 2
					If MapTemp(x, y) = 0 Then
						
						If (Min(MapTemp(x + 1, y),1) + Min(MapTemp(x - 1, y),1) + Min(MapTemp(x, y + 1),1) + Min(MapTemp(x, y - 1),1)) = 1 Then
							;If Rand(4)=1 Then
							
							If MapTemp(x + 1, y) Then
								x2 = x+1 : y2 = y
							ElseIf MapTemp(x - 1, y)
								x2 = x-1 : y2 = y
							ElseIf MapTemp(x, y+1)
								x2 = x : y2 = y+1	
							ElseIf MapTemp(x, y-1)
								x2 = x : y2 = y-1
							EndIf
							
							placed = False
							If MapTemp(x2,y2)>1 And MapTemp(x2,y2)<4 Then 
								Select MapTemp(x2,y2)
									Case 2
										If Min(MapTemp(x2 + 1, y2),1) + Min(MapTemp(x2 - 1, y2),1)= 2 Then
											Room2Amount[i]=Room2Amount[i]-1
											Room3Amount[i]=Room3Amount[i]+1
											placed = True
										ElseIf Min(MapTemp(x2, y2 + 1),1) + Min(MapTemp(x2, y2 - 1),1)= 2
											Room2Amount[i]=Room2Amount[i]-1
											Room3Amount[i]=Room3Amount[i]+1
											placed = True
										EndIf
									Case 3
										Room3Amount[i]=Room3Amount[i]-1
										Room4Amount[i]=Room4Amount[i]+1	
										placed = True
								End Select
								
								If placed Then
									MapTemp(x2,y2)=MapTemp(x2,y2)+1
									
									MapTemp(x, y) = 1
									Room1Amount[i] = Room1Amount[i]+1	
									
									temp=temp-1
								EndIf
							EndIf
						EndIf
						
					EndIf
					If temp = 0 Then Exit
				Next
				If temp = 0 Then Exit
			Next
		EndIf
	Next
	
	
	
	
	
	;force more room4s and room2Cs
	For i = 0 To 2
		
		Select i
			Case 2
				zone=2
				temp2=MapHeight/3;-1
			Case 1
				zone=MapHeight/3+1
				temp2=MapHeight*(2.0/3.0)-1
			Case 0
				zone=MapHeight*(2.0/3.0)+1
				temp2=MapHeight-2
		End Select
		
		If Room4Amount[i]<1 Then ;we want at least 1 ROOM4
			DebugLog "forcing a ROOM4 into zone "+i
			temp=0
			
			For y = zone To temp2
				For x = 2 To MapWidth - 2
					If MapTemp(x,y)=3 Then
						Select 0 ;see if adding a ROOM1 is possible
							Case (MapTemp(x+1,y) Or MapTemp(x+1,y+1) Or MapTemp(x+1,y-1) Or MapTemp(x+2,y))
								MapTemp(x+1,y)=1
								temp=1
							Case (MapTemp(x-1,y) Or MapTemp(x-1,y+1) Or MapTemp(x-1,y-1) Or MapTemp(x-2,y))
								MapTemp(x-1,y)=1
								temp=1
							Case (MapTemp(x,y+1) Or MapTemp(x+1,y+1) Or MapTemp(x-1,y+1) Or MapTemp(x,y+2))
								MapTemp(x,y+1)=1
								temp=1
							Case (MapTemp(x,y-1) Or MapTemp(x+1,y-1) Or MapTemp(x-1,y-1) Or MapTemp(x,y-2))
								MapTemp(x,y-1)=1
								temp=1
						End Select
						If temp=1 Then
							MapTemp(x,y)=4 ;turn this room into a ROOM4
							DebugLog "ROOM4 forced into slot ("+x+", "+y+")"
							Room4Amount[i]=Room4Amount[i]+1
							Room3Amount[i]=Room3Amount[i]-1
							Room1Amount[i]=Room1Amount[i]+1
						EndIf
					EndIf
					If temp=1 Then Exit
				Next
				If temp=1 Then Exit
			Next
			
			If temp=0 Then DebugLog "Couldn't place ROOM4 in zone "+i
		EndIf
		
		If Room2CAmount[i]<1 Then ;we want at least 1 ROOM2C
			DebugLog "forcing a ROOM2C into zone "+i
			temp=0
			
			zone=zone+1
			temp2=temp2-1
			
			For y = zone To temp2
				For x = 3 To MapWidth - 3
					If MapTemp(x,y)=1 Then
						Select True ;see if adding some rooms is possible
							Case MapTemp(x-1,y)>0
								If (MapTemp(x,y-1)+MapTemp(x,y+1)+MapTemp(x+2,y))=0 Then
									If (MapTemp(x+1,y-2)+MapTemp(x+2,y-1)+MapTemp(x+1,y-1))=0 Then
										MapTemp(x,y)=2
										MapTemp(x+1,y)=2
										DebugLog "ROOM2C forced into slot ("+(x+1)+", "+(y)+")"
										MapTemp(x+1,y-1)=1
										temp=1
									Else If (MapTemp(x+1,y+2)+MapTemp(x+2,y+1)+MapTemp(x+1,y+1))=0 Then
										MapTemp(x,y)=2
										MapTemp(x+1,y)=2
										DebugLog "ROOM2C forced into slot ("+(x+1)+", "+(y)+")"
										MapTemp(x+1,y+1)=1
										temp=1
									EndIf
								EndIf
							Case MapTemp(x+1,y)>0
								If (MapTemp(x,y-1)+MapTemp(x,y+1)+MapTemp(x-2,y))=0 Then
									If (MapTemp(x-1,y-2)+MapTemp(x-2,y-1)+MapTemp(x-1,y-1))=0 Then
										MapTemp(x,y)=2
										MapTemp(x-1,y)=2
										DebugLog "ROOM2C forced into slot ("+(x-1)+", "+(y)+")"
										MapTemp(x-1,y-1)=1
										temp=1
									Else If (MapTemp(x-1,y+2)+MapTemp(x-2,y+1)+MapTemp(x-1,y+1))=0 Then
										MapTemp(x,y)=2
										MapTemp(x-1,y)=2
										DebugLog "ROOM2C forced into slot ("+(x-1)+", "+(y)+")"
										MapTemp(x-1,y+1)=1
										temp=1
									EndIf
								EndIf
							Case MapTemp(x,y-1)>0
								If (MapTemp(x-1,y)+MapTemp(x+1,y)+MapTemp(x,y+2))=0 Then
									If (MapTemp(x-2,y+1)+MapTemp(x-1,y+2)+MapTemp(x-1,y+1))=0 Then
										MapTemp(x,y)=2
										MapTemp(x,y+1)=2
										DebugLog "ROOM2C forced into slot ("+(x)+", "+(y+1)+")"
										MapTemp(x-1,y+1)=1
										temp=1
									Else If (MapTemp(x+2,y+1)+MapTemp(x+1,y+2)+MapTemp(x+1,y+1))=0 Then
										MapTemp(x,y)=2
										MapTemp(x,y+1)=2
										DebugLog "ROOM2C forced into slot ("+(x)+", "+(y+1)+")"
										MapTemp(x+1,y+1)=1
										temp=1
									EndIf
								EndIf
							Case MapTemp(x,y+1)>0
								If (MapTemp(x-1,y)+MapTemp(x+1,y)+MapTemp(x,y-2))=0 Then
									If (MapTemp(x-2,y-1)+MapTemp(x-1,y-2)+MapTemp(x-1,y-1))=0 Then
										MapTemp(x,y)=2
										MapTemp(x,y-1)=2
										DebugLog "ROOM2C forced into slot ("+(x)+", "+(y-1)+")"
										MapTemp(x-1,y-1)=1
										temp=1
									Else If (MapTemp(x+2,y-1)+MapTemp(x+1,y-2)+MapTemp(x+1,y-1))=0 Then
										MapTemp(x,y)=2
										MapTemp(x,y-1)=2
										DebugLog "ROOM2C forced into slot ("+(x)+", "+(y-1)+")"
										MapTemp(x+1,y-1)=1
										temp=1
									EndIf
								EndIf
						End Select
						If temp=1 Then
							Room2CAmount[i]=Room2CAmount[i]+1
							Room2Amount[i]=Room2Amount[i]+1
						EndIf
					EndIf
					If temp=1 Then Exit
				Next
				If temp=1 Then Exit
			Next
			
			If temp=0 Then DebugLog "Couldn't place ROOM2C in zone "+i
		EndIf
		
	Next
	
	Local MaxRooms% = 55*MapWidth/20
	MaxRooms=Max(MaxRooms,Room1Amount[0]+Room1Amount[1]+Room1Amount[2]+1)
	MaxRooms=Max(MaxRooms,Room2Amount[0]+Room2Amount[1]+Room2Amount[2]+1)
	MaxRooms=Max(MaxRooms,Room2CAmount[0]+Room2CAmount[1]+Room2CAmount[2]+1)
	MaxRooms=Max(MaxRooms,Room3Amount[0]+Room3Amount[1]+Room3Amount[2]+1)
	MaxRooms=Max(MaxRooms,Room4Amount[0]+Room4Amount[1]+Room4Amount[2]+1)
	Dim MapRoom$(ROOM4 + 1, MaxRooms)
	
	
	;zone 1 --------------------------------------------------------------------------------------------------
	
	Local min_pos = 1, max_pos = Room1Amount[0]-1
	
	MapRoom(ROOM1, 0) = "start"	
	SetRoom("roompj", ROOM1, Floor(0.1*Float(Room1Amount[0])),min_pos,max_pos)
	SetRoom("914", ROOM1, Floor(0.3*Float(Room1Amount[0])),min_pos,max_pos)
	SetRoom("room1archive",ROOM1,Floor(0.5*Float(Room1Amount[0])),min_pos,max_pos)
	SetRoom("room205", ROOM1, Floor(0.6*Float(Room1Amount[0])),min_pos,max_pos)
	SetRoom("room178",ROOM1,Floor(0.7*Float(Room1Amount[0])),min_pos,max_pos)
	
	MapRoom(ROOM2C, 0) = "lockroom"
	
	min_pos = 1
	max_pos = Room2Amount[0]-1
	
	MapRoom(ROOM2, 0) = "room2closets"
	SetRoom("room2testroom2", ROOM2, Floor(0.1*Float(Room2Amount[0])),min_pos,max_pos)
	SetRoom("room2scps", ROOM2, Floor(0.2*Float(Room2Amount[0])),min_pos,max_pos)
	SetRoom("room2storage", ROOM2, Floor(0.3*Float(Room2Amount[0])),min_pos,max_pos)
	SetRoom("room012", ROOM2, Floor(0.55*Float(Room2Amount[0])),min_pos,max_pos)
	SetRoom("room1123",ROOM2,Floor(0.7*Float(Room2Amount[0])),min_pos,max_pos)
	SetRoom("room2elevator",ROOM2,Floor(0.85*Float(Room2Amount[0])),min_pos,max_pos)
	;If (Rand(0,100)<2) Then SetRoom("room2test1074",ROOM2,Floor(0.95*Float(Room2Amount[0])),min_pos,max_pos) ;yeah, no, i'm not giving you any chances
	SetRoom("room2scps2",ROOM2,Floor(0.6*Float(Room2Amount[0])),min_pos,max_pos)
	SetRoom("room2gw_b", ROOM2, Floor(0.4*Float(Room2Amount[0])),min_pos,max_pos)
	SetRoom("room2sl", ROOM2, Floor(0.5*Float(Room2Amount[0])),min_pos,max_pos)
	
	MapRoom(ROOM3, Floor(Rnd(0.2,0.8)*Float(Room3Amount[0]))) = "room3storage"
	
	MapRoom(ROOM2C, Floor(0.5*Float(Room2CAmount[0]))) = "room1162"
	
	;zone 2 --------------------------------------------------------------------------------------------------
	
	min_pos = Room1Amount[0]
	max_pos = Room1Amount[0]+Room1Amount[1]-1	
	
	MapRoom(ROOM1, Room1Amount[0]+Floor(0.1*Float(Room1Amount[1]))) = "room079"
	SetRoom("room106", ROOM1, Room1Amount[0]+Floor(0.3*Float(Room1Amount[1])),min_pos,max_pos)
	SetRoom("coffin", ROOM1, Room1Amount[0]+Floor(0.5*Float(Room1Amount[1])),min_pos,max_pos)
	SetRoom("room035", ROOM1, Room1Amount[0]+Floor(0.7*Float(Room1Amount[1])),min_pos,max_pos)
	SetRoom("008", ROOM1, Room1Amount[0]+Floor(0.9*Float(Room1Amount[1])),min_pos,max_pos)
	
	min_pos = Room2Amount[0]
	max_pos = Room2Amount[0]+Room2Amount[1]-1
	
	MapRoom(ROOM2, Room2Amount[0]+Floor(0.1*Float(Room2Amount[1]))) = "room2nuke"
	SetRoom("room2tunnel", ROOM2, Room2Amount[0]+Floor(0.25*Float(Room2Amount[1])),min_pos,max_pos)
	SetRoom("room049", ROOM2, Room2Amount[0]+Floor(0.4*Float(Room2Amount[1])),min_pos,max_pos)
	SetRoom("room2servers", ROOM2, Room2Amount[0]+Floor(0.7*Room2Amount[1]),min_pos,max_pos)
	SetRoom("testroom", ROOM2, Room2Amount[0]+Floor(0.9*Float(Room2Amount[1])),min_pos,max_pos)
	
	MapRoom(ROOM3, Room3Amount[0]+Floor(0.3*Float(Room3Amount[1]))) = "room513"
	MapRoom(ROOM3, Room3Amount[0]+Floor(0.6*Float(Room3Amount[1]))) = "room966"
	
	
	;zone 3  --------------------------------------------------------------------------------------------------
	
	MapRoom(ROOM1, Room1Amount[0]+Room1Amount[1]+Room1Amount[2]-2) = "exit1"
	MapRoom(ROOM1, Room1Amount[0]+Room1Amount[1]+Room1Amount[2]-1) = "gateaentrance"
	MapRoom(ROOM1, Room1Amount[0]+Room1Amount[1]) = "room1lifts"
	
	min_pos = Room2Amount[0]+Room2Amount[1]
	max_pos = Room2Amount[0]+Room2Amount[1]+Room2Amount[2]-1		
	
	MapRoom(ROOM2, min_pos+Floor(0.1*Float(Room2Amount[2]))) = "room2poffices"
	SetRoom("room2cafeteria", ROOM2, min_pos+Floor(0.2*Float(Room2Amount[2])),min_pos,max_pos)
	SetRoom("room2sroom", ROOM2, min_pos+Floor(0.3*Float(Room2Amount[2])),min_pos,max_pos)
	SetRoom("room2offices", ROOM2, min_pos+Floor(0.45*Room2Amount[2]),min_pos,max_pos)
	SetRoom("room860", ROOM2, min_pos+Floor(0.6*Room2Amount[2]),min_pos,max_pos)
	SetRoom("room2poffices2", ROOM2, min_pos+Floor(0.8*Room2Amount[2]),min_pos,max_pos)
	SetRoom("room2offices2", ROOM2, min_pos+Floor(0.9*Float(Room2Amount[2])),min_pos,max_pos)
	SetRoom("room2servers2", ROOM2, min_pos+Floor(0.4*Room2Amount[2]),min_pos,max_pos)
	SetRoom("room2offices4", ROOM2, min_pos+Floor(0.5*Room2Amount[2]),min_pos,max_pos)
	
	MapRoom(ROOM2C, Room2CAmount[0]+Room2CAmount[1]) = "room2ccont"	
	MapRoom(ROOM2C, Room2CAmount[0]+Room2CAmount[1]+1) = "lockroom2"		
	
	MapRoom(ROOM3, Room3Amount[0]+Room3Amount[1]+Floor(0.3*Float(Room3Amount[2]))) = "room3servers"
	MapRoom(ROOM3, Room3Amount[0]+Room3Amount[1]+Floor(0.7*Float(Room3Amount[2]))) = "room3servers2"
	;MapRoom(ROOM3, Room3Amount[0]+Room3Amount[1]) = "room3gw"
	MapRoom(ROOM3, Room3Amount[0]+Room3Amount[1]+Floor(0.5*Float(Room3Amount[2]))) = "room3offices"
	
	;----------------------- luodaan kartta --------------------------------
	
	temp = 0
	Local r.Rooms, spacing# = 8.0
	For y = MapHeight - 1 To 1 Step - 1
		
		;zone% = GetZone(y)
		
		If y < MapHeight/3+1 Then
			zone=3
		ElseIf y < MapHeight*(2.0/3.0);-1
			zone=2
		Else
			zone=1
		EndIf
		
		For x = 1 To MapWidth - 2
			If MapTemp(x, y) = 255 Then
				If y>MapHeight/2 Then ;zone = 2
					r = CreateRoom(zone, ROOM2, x * 8, 0, y * 8, "checkpoint1")
				Else ;If zone = 3
					r = CreateRoom(zone, ROOM2, x * 8, 0, y * 8, "checkpoint2")
				EndIf
				
			ElseIf MapTemp(x, y) > 0
				
				temp = Min(MapTemp(x + 1, y),1) + Min(MapTemp(x - 1, y),1) + Min(MapTemp(x, y + 1),1) + Min(MapTemp(x, y - 1),1)
				
				Select temp ;viereisiss� ruuduissa olevien huoneiden m��r�
					Case 1
						If MapRoomID(ROOM1) < MaxRooms And MapName(x,y) = "" Then
							If CheckRoomOverlap(MapRoom(ROOM1, MapRoomID(ROOM1)), x, y) Then
								For i = MapRoomID(ROOM1)+1 To MaxRooms
									If MapRoom(ROOM1, i)="" Then MapRoom(ROOM1, i)=MapRoom(ROOM1, MapRoomID(ROOM1)) : Exit
								Next
								MapRoom(ROOM1, MapRoomID(ROOM1))=""
							Else
								If MapRoom(ROOM1, MapRoomID(ROOM1)) <> "" Then MapName(x, y) = MapRoom(ROOM1, MapRoomID(ROOM1))	
							EndIf
						EndIf
						
						r = CreateRoom(zone, ROOM1, x * 8, 0, y * 8, MapName(x, y))
						If MapTemp(x, y + 1) Then
							r\angle = 180 
							TurnEntity(r\obj, 0, r\angle, 0)
						ElseIf MapTemp(x - 1, y)
							r\angle = 270
							TurnEntity(r\obj, 0, r\angle, 0)
						ElseIf MapTemp(x + 1, y)
							r\angle = 90
							TurnEntity(r\obj, 0, r\angle, 0)
						Else 
							r\angle = 0
						End If
						
						MapRoomID(ROOM1)=MapRoomID(ROOM1)+1
					Case 2
						If MapTemp(x - 1, y)>0 And MapTemp(x + 1, y)>0 Then
							If MapRoomID(ROOM2) < MaxRooms And MapName(x,y) = ""  Then
								If CheckRoomOverlap(MapRoom(ROOM2, MapRoomID(ROOM2)), x, y) Then
									For i = MapRoomID(ROOM2)+1 To MaxRooms
										If MapRoom(ROOM2, i)="" Then MapRoom(ROOM2, i)=MapRoom(ROOM2, MapRoomID(ROOM2)) : Exit
									Next
									MapRoom(ROOM2, MapRoomID(ROOM2))=""
								Else
									If MapRoom(ROOM2, MapRoomID(ROOM2)) <> "" Then MapName(x, y) = MapRoom(ROOM2, MapRoomID(ROOM2))	
								EndIf
							EndIf
							r = CreateRoom(zone, ROOM2, x * 8, 0, y * 8, MapName(x, y))
							If Rand(2) = 1 Then r\angle = 90 Else r\angle = 270
							TurnEntity(r\obj, 0, r\angle, 0)
							MapRoomID(ROOM2)=MapRoomID(ROOM2)+1
						ElseIf MapTemp(x, y - 1)>0 And MapTemp(x, y + 1)>0
							If MapRoomID(ROOM2) < MaxRooms And MapName(x,y) = ""  Then
								If CheckRoomOverlap(MapRoom(ROOM2, MapRoomID(ROOM2)), x, y) Then
									For i = MapRoomID(ROOM2)+1 To MaxRooms
										If MapRoom(ROOM2, i)="" Then MapRoom(ROOM2, i)=MapRoom(ROOM2, MapRoomID(ROOM2)) : Exit
									Next
									MapRoom(ROOM2, MapRoomID(ROOM2))=""
								Else
									If MapRoom(ROOM2, MapRoomID(ROOM2)) <> "" Then MapName(x, y) = MapRoom(ROOM2, MapRoomID(ROOM2))	
								EndIf
							EndIf
							r = CreateRoom(zone, ROOM2, x * 8, 0, y * 8, MapName(x, y))
							If Rand(2) = 1 Then r\angle = 180 Else r\angle = 0
							TurnEntity(r\obj, 0, r\angle, 0)								
							MapRoomID(ROOM2)=MapRoomID(ROOM2)+1
						Else
							If MapRoomID(ROOM2C) < MaxRooms And MapName(x,y) = ""  Then
								If CheckRoomOverlap(MapRoom(ROOM2C, MapRoomID(ROOM2C)), x, y) Then
									For i = MapRoomID(ROOM2C)+1 To MaxRooms
										If MapRoom(ROOM2C, i)="" Then MapRoom(ROOM2C, i)=MapRoom(ROOM2C, MapRoomID(ROOM2C)) : Exit
									Next
									MapRoom(ROOM2C, MapRoomID(ROOM2C))=""
								Else
									If MapRoom(ROOM2C, MapRoomID(ROOM2C)) <> "" Then MapName(x, y) = MapRoom(ROOM2C, MapRoomID(ROOM2C))	
								EndIf
							EndIf
							
							If MapTemp(x - 1, y)>0 And MapTemp(x, y + 1)>0 Then
								r = CreateRoom(zone, ROOM2C, x * 8, 0, y * 8, MapName(x, y))
								r\angle = 180
								TurnEntity(r\obj, 0, r\angle, 0)
								MapRoomID(ROOM2C)=MapRoomID(ROOM2C)+1
							ElseIf MapTemp(x + 1, y)>0 And MapTemp(x, y + 1)>0
								r = CreateRoom(zone, ROOM2C, x * 8, 0, y * 8, MapName(x, y))
								r\angle = 90
								TurnEntity(r\obj, 0, r\angle, 0)
								MapRoomID(ROOM2C)=MapRoomID(ROOM2C)+1		
							ElseIf MapTemp(x - 1, y)>0 And MapTemp(x, y - 1)>0
								r = CreateRoom(zone, ROOM2C, x * 8, 0, y * 8, MapName(x, y))
								TurnEntity(r\obj, 0, 270, 0)
								r\angle = 270
								MapRoomID(ROOM2C)=MapRoomID(ROOM2C)+1		
							Else
								r = CreateRoom(zone, ROOM2C, x * 8, 0, y * 8, MapName(x, y))
								MapRoomID(ROOM2C)=MapRoomID(ROOM2C)+1
							EndIf
						EndIf
					Case 3
						If MapRoomID(ROOM3) < MaxRooms And MapName(x,y) = ""  Then
							If CheckRoomOverlap(MapRoom(ROOM3, MapRoomID(ROOM3)), x, y) Then
								For i = MapRoomID(ROOM3)+1 To MaxRooms
									If MapRoom(ROOM3, i)="" Then MapRoom(ROOM3, i)=MapRoom(ROOM3, MapRoomID(ROOM3)) : Exit
								Next
								MapRoom(ROOM3, MapRoomID(ROOM3))=""
							Else
								If MapRoom(ROOM3, MapRoomID(ROOM3)) <> "" Then MapName(x, y) = MapRoom(ROOM3, MapRoomID(ROOM3))	
							EndIf
						EndIf
						
						r = CreateRoom(zone, ROOM3, x * 8, 0, y * 8, MapName(x, y))
						If (Not MapTemp(x, y - 1)) Then
							TurnEntity(r\obj, 0, 180, 0)
							r\angle = 180
						ElseIf (Not MapTemp(x - 1, y))
							TurnEntity(r\obj, 0, 90, 0)
							r\angle = 90
						ElseIf (Not MapTemp(x + 1, y))
							TurnEntity(r\obj, 0, -90, 0)
							r\angle = 270
						End If
						MapRoomID(ROOM3)=MapRoomID(ROOM3)+1
					Case 4
						If MapRoomID(ROOM4) < MaxRooms And MapName(x,y) = ""  Then
							If CheckRoomOverlap(MapRoom(ROOM4, MapRoomID(ROOM4)), x, y) Then
								For i = MapRoomID(ROOM4)+1 To MaxRooms
									If MapRoom(ROOM4, i)="" Then MapRoom(ROOM4, i)=MapRoom(ROOM4, MapRoomID(ROOM4)) : Exit
								Next
								MapRoom(ROOM4, MapRoomID(ROOM4))=""
							Else
								If MapRoom(ROOM4, MapRoomID(ROOM4)) <> "" Then MapName(x, y) = MapRoom(ROOM4, MapRoomID(ROOM4))	
							EndIf
						EndIf
						
						r = CreateRoom(zone, ROOM4, x * 8, 0, y * 8, MapName(x, y))
						MapRoomID(ROOM4)=MapRoomID(ROOM4)+1
				End Select
				
			End If
			
			If MapTemp(x, y)>0 Then
				If (Floor((x + y) / 2.0) = Ceil((x + y) / 2.0)) Then
					If zone = 2 Then temp = 2 Else temp=0
					
					If MapTemp(x + 1, y) Then
						d.Doors = CreateDoor(r\zone, Float(x) * spacing + spacing / 2.0, 0, Float(y) * spacing, 90, r, Max(Rand(-3, 1), 0), temp)
						r\AdjDoor[0] = d
					EndIf
					
					If MapTemp(x - 1, y) Then
						d.Doors = CreateDoor(r\zone, Float(x) * spacing - spacing / 2.0, 0, Float(y) * spacing, 90, r, Max(Rand(-3, 1), 0), temp)
						r\AdjDoor[2] = d
					EndIf
					
					If MapTemp(x, y + 1) Then
						d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing + spacing / 2.0, 0, r, Max(Rand(-3, 1), 0), temp)
						r\AdjDoor[3] = d
					EndIf
					
					If MapTemp(x, y - 1) Then
						d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing - spacing / 2.0, 0, r, Max(Rand(-3, 1), 0), temp)
						r\AdjDoor[1] = d
					EndIf
				End If
			EndIf
			
		Next
	Next		
	
	r = CreateRoom(0, ROOM1, 0, 0, 8, "gatea")
	MapRoomID(ROOM1)=MapRoomID(ROOM1)+1
	
	r = CreateRoom(0, ROOM1, (MapWidth-1) * 8, 0, (MapHeight-1) * 8, "pocketdimension")
	MapRoomID(ROOM1)=MapRoomID(ROOM1)+1	
	
	r = CreateRoom(0, ROOM1, 8, 0, (MapHeight-1) * 8, "173")
	MapRoomID(ROOM1)=MapRoomID(ROOM1)+1
	
	r = CreateRoom(0, ROOM1, 8, 800, 0, "dimension1499")
	MapRoomID(ROOM1)=MapRoomID(ROOM1)+1
	
	If 0 Then 
		Repeat
			Cls
			For x = 0 To MapWidth - 1
				For y = 0 To MapHeight - 1
					If MapTemp(x, y) = 0 Then
						
						zone=GetZone(y)
						
						Color 50*zone, 50*zone, 50*zone
						Rect(x * 32, y * 32, 30, 30)
					Else
						If MapTemp(x, y) = 255 Then
							Color 0,200,0
						Else If MapTemp(x,y)=4 Then
							Color 50,50,255
						Else If MapTemp(x,y)=3 Then
							Color 50,255,255
						Else If MapTemp(x,y)=2 Then
							Color 255,255,50
						Else
							Color 255, 255, 255
						EndIf
						Rect(x * 32, y * 32, 30, 30)
					End If
				Next
			Next	
			
			For x = 0 To MapWidth - 1
				For y = 0 To MapHeight - 1
					
					If MouseX()>x*32 And MouseX()<x*32+32 Then
						If MouseY()>y*32 And MouseY()<y*32+32 Then
							Color 255, 0, 0
						Else
							Color 200, 200, 200
						EndIf
					Else
						Color 200, 200, 200
					EndIf
					
					If MapTemp(x, y) > 0 Then
						Text x * 32 +2, (y) * 32 + 2,MapTemp(x, y) +" "+ MapName(x,y)
					End If
				Next
			Next			
			
			Flip
		Until KeyHit(28)		
	EndIf
	
	
	For y = 0 To MapHeight
		For x = 0 To MapWidth
			MapTemp(x, y) = Min(MapTemp(x, y),1)
		Next
	Next
	
	For r.Rooms = Each Rooms
		r\Adjacent[0]=Null
		r\Adjacent[1]=Null
		r\Adjacent[2]=Null
		r\Adjacent[3]=Null
		For r2.Rooms = Each Rooms
			If r<>r2 Then
				If r2\z=r\z Then
					If (r2\x)=(r\x+8.0) Then
						r\Adjacent[0]=r2
						If r\AdjDoor[0] = Null Then r\AdjDoor[0] = r2\AdjDoor[2]
					ElseIf (r2\x)=(r\x-8.0)
						r\Adjacent[2]=r2
						If r\AdjDoor[2] = Null Then r\AdjDoor[2] = r2\AdjDoor[0]
					EndIf
				ElseIf r2\x=r\x Then
					If (r2\z)=(r\z-8.0) Then
						r\Adjacent[1]=r2
						If r\AdjDoor[1] = Null Then r\AdjDoor[1] = r2\AdjDoor[3]
					ElseIf (r2\z)=(r\z+8.0)
						r\Adjacent[3]=r2
						If r\AdjDoor[3] = Null Then r\AdjDoor[3] = r2\AdjDoor[1]
					EndIf
				EndIf
			EndIf
			If (r\Adjacent[0]<>Null) And (r\Adjacent[1]<>Null) And (r\Adjacent[2]<>Null) And (r\Adjacent[3]<>Null) Then Exit
		Next
	Next
	
End Function


Function CheckRoomOverlap(roomname$, x%, y%)
	Return False
	
	roomname = Lower(roomname)
	
	Local rt.RoomTemplates
	For rt.RoomTemplates = Each RoomTemplates
		If rt\Name = roomname Then
			If (Not rt\Large) Then Return False
			
			For x2= Max(0,x-1) To Min(MapWidth-1,x+1)
				For y2= Max(0,y-1) To Min(MapHeight-1,y+1)
					If x2<>x And y2<>y Then
						If MapTemp(x2,y2)>1 Then Return True
					EndIf
				Next
			Next
			
			Return False
		EndIf
	Next
End Function

Function SetRoom(room_name$,room_type%,pos%,min_pos%,max_pos%) ;place a room without overwriting others
	
	If max_pos<min_pos Then DebugLog "Can't place "+room_name : Return False
	
	DebugLog "--- SETROOM: "+Upper(room_name)+" ---"
	Local looped%,can_place%
	looped = False
	can_place = True
	While MapRoom(room_type,pos)<>""
		DebugLog "found "+MapRoom(room_type,pos)
		pos=pos+1
		If pos>max_pos Then
			If looped=False Then
				pos=min_pos+1 : looped=True
			Else
				can_place=False
				Exit
			EndIf
		EndIf
	Wend
	DebugLog room_name+" "+Str(pos)
	If can_place=True Then
		DebugLog "--------------"
		MapRoom(room_type,pos)=room_name
		Return True
	Else
		DebugLog "couldn't place "+room_name
		Return False
	EndIf
End Function

Function GetZone(y%)
	Return Min(Floor((Float(MapWidth-y)/MapWidth*ZONEAMOUNT)),ZONEAMOUNT-1)
End Function

;-------------------------------------------------------------------------------------------------------


Function load_terrain(hmap,yscale#=0.7,t1%,t2%,mask%)
	
	DebugLog "load_terrain: "+hmap
	
	; load the heightmap
	If hmap = 0 Then RuntimeError "Heightmap image "+hmap+" does not exist."
	
	; store heightmap dimensions
	Local x = ImageWidth(hmap)-1, y = ImageHeight(hmap)-1
	Local lx,ly,index
	
	; load texture and lightmaps
	If t1 = 0 Then RuntimeError "load_terrain error: invalid texture 1"
	If t2 = 0 Then RuntimeError "load_terrain error: invalid texture 2"
	If mask = 0 Then RuntimeError "load_terrain error: invalid texture mask"
	
	; auto scale the textures to the right size
	If t1 Then ScaleTexture t1,x/4,y/4
	If t2 Then ScaleTexture t2,x/4,y/4
	If mask Then ScaleTexture mask,x,y
	
	; start building the terrain
	Local mesh = CreateMesh()
	Local surf = CreateSurface(mesh)
	
	; create some verts for the terrain
	For ly = 0 To y
		For lx = 0 To x
			AddVertex surf,lx,0,ly,1.0/lx,1.0/ly
		Next
	Next
	RenderWorld
			
	; connect the verts with faces
	For ly = 0 To y-1
		For lx = 0 To x-1
			AddTriangle surf,lx+((x+1)*ly),lx+((x+1)*ly)+(x+1),(lx+1)+((x+1)*ly)
			AddTriangle surf,(lx+1)+((x+1)*ly),lx+((x+1)*ly)+(x+1),(lx+1)+((x+1)*ly)+(x+1)
		Next
	Next
			
	; position the terrain to center 0,0,0
	Local mesh2% = CopyMesh(mesh,mesh)
	Local surf2% = GetSurface(mesh2,1)
	PositionMesh mesh, -x/2.0,0,-y/2.0
	PositionMesh mesh2, -x/2.0,0.01,-y/2.0
	
	; alter vertice height to match the heightmap red channel
	LockBuffer ImageBuffer(hmap)
	LockBuffer TextureBuffer(mask)
	;SetBuffer 
	For lx = 0 To x
		For ly = 0 To y
			;using vertex alpha and two meshes instead of FE_ALPHAWHATEVER
			;it doesn't look perfect but it does the job
			;you might get better results by downscaling the mask to the same size as the heightmap
			Local maskX# = Min(lx*Float(TextureWidth(mask))/Float(ImageWidth(hmap)),TextureWidth(mask)-1)
			Local maskY# = TextureHeight(mask)-Min(ly*Float(TextureHeight(mask))/Float(ImageHeight(hmap)),TextureHeight(mask)-1)
			RGB1=ReadPixelFast(Min(lx,x-1),y-Min(ly,y-1),ImageBuffer(hmap))
			r=(RGB1 And $FF0000)Shr 16 ;separate out the red
			Local alpha#=(((ReadPixelFast(Max(maskX-5,5),Max(maskY-5,5),TextureBuffer(mask)) And $FF000000) Shr 24)/$FF)
			alpha#=alpha+(((ReadPixelFast(Min(maskX+5,TextureWidth(mask)-5),Min(maskY+5,TextureHeight(mask)-5),TextureBuffer(mask)) And $FF000000) Shr 24)/$FF)
			alpha#=alpha+(((ReadPixelFast(Max(maskX-5,5),Min(maskY+5,TextureHeight(mask)-5),TextureBuffer(mask)) And $FF000000) Shr 24)/$FF)
			alpha#=alpha+(((ReadPixelFast(Min(maskX+5,TextureWidth(mask)-5),Max(maskY-5,5),TextureBuffer(mask)) And $FF000000) Shr 24)/$FF)
			alpha#=alpha*0.25
			alpha#=Sqr(alpha)
			
			index = lx + ((x+1)*ly)
			VertexCoords surf, index , VertexX(surf,index), r*yscale,VertexZ(surf,index)
			VertexCoords surf2, index , VertexX(surf2,index), r*yscale,VertexZ(surf2,index)
			VertexColor surf2, index, 255.0,255.0,255.0,alpha
			; set the terrain texture coordinates
			VertexTexCoords surf,index,lx,-ly 
			VertexTexCoords surf2,index,lx,-ly 
		Next
	Next
	UnlockBuffer TextureBuffer(mask)
	UnlockBuffer ImageBuffer(hmap)
	
	UpdateNormals mesh
	UpdateNormals mesh2
	
	EntityTexture mesh,t1,0,0
	;EntityTexture mesh,mask,0,1
	EntityTexture mesh2,t2,0,0;2
	
	EntityFX mesh, 1
	EntityFX mesh2, 1+2+32
	
	Return mesh
End Function



Include "Skybox.bb"


Function UpdateRoomLights(cam%)
	
	Local r.Rooms, i, random#, alpha#, dist#
	
	For r.Rooms = Each Rooms
		If r\dist < HideDistance*0.7 Or r = PlayerRoom
			For i = 0 To r\MaxLights%
				If r\Lights%[i]<>0
					If userOptions\roomLights
						If EntityDistance(cam%,r\Lights%[i])<8.5
							If r\LightHidden[i]
								ShowEntity r\Lights%[i]
								r\LightHidden[i] = False
							EndIf
						Else
							If (Not r\LightHidden[i])
								HideEntity r\Lights%[i]
								r\LightHidden[i] = True
							EndIf
						EndIf
						
						If EntityDistance(cam%,r\LightSprites2[i])<8.5
							If EntityVisible(cam%,r\LightSpritesPivot[i])
								If r\LightSpriteHidden%[i]
									ShowEntity r\LightSprites2%[i]
									r\LightSpriteHidden%[i] = False
								EndIf
								If PlayerRoom\RoomTemplate\Name$ = "173"
									random# = Rnd(0.38,0.42)
								Else
									If r\LightFlicker%[i]<5
										random# = Rnd(0.38,0.42)
									ElseIf r\LightFlicker%[i]>4 And r\LightFlicker%[i]<10
										random# = Rnd(0.35,0.45)
									Else
										random# = Rnd(0.3,0.5)
									EndIf
								EndIf
								ScaleSprite r\LightSprites2[i],random#,random#
								dist# = (EntityDistance(cam%,r\LightSpritesPivot[i])+0.5)/7.5
								dist# = Max(Min(dist#,1.0),0.0)
								alpha# = Float(Inverse(dist#))
								
								If alpha# > 0.0
									EntityAlpha r\LightSprites2[i],Max(3*(Brightness/255)*(r\LightIntensity[i]/2),1)*alpha#
								Else
									;Instead of rendering the sprite invisible, just hiding it if the player is far away from it
									If (Not r\LightSpriteHidden%[i])
										HideEntity r\LightSprites2[i]
										r\LightSpriteHidden%[i]=True
									EndIf
								EndIf
							Else
								If (Not r\LightSpriteHidden%[i])
									HideEntity r\LightSprites2%[i]
									r\LightSpriteHidden%[i] = True
								EndIf
							EndIf
						Else
							If (Not r\LightSpriteHidden%[i])
								HideEntity r\LightSprites2%[i]
								r\LightSpriteHidden%[i] = True
							EndIf
						EndIf
					Else
						If (Not r\LightHidden[i])
							HideEntity r\Lights%[i]
							r\LightHidden[i] = True
						EndIf
						If (Not r\LightSpriteHidden[i])
							HideEntity r\LightSprites2[i]
							r\LightSpriteHidden[i]=True
						EndIf
					EndIf
				EndIf
			Next
		EndIf
	Next
	
End Function

Function UpdateCheckpointMonitors(numb%)
	Local i,sf,b,t1
	Local entity%
	
	If numb% = 0
		entity% = Monitor2
		UpdateCheckpoint1 = True
	Else
		entity% = Monitor3
		UpdateCheckpoint2 = True
	EndIf
	
	For i = 2 To CountSurfaces(entity)
		sf = GetSurface(entity,i)
		b = GetSurfaceBrush(sf)
		If b<>0 Then
			t1 = GetBrushTexture(b,0)
			If t1<>0 Then
				name$ = StripPath(TextureName(t1))
				If Lower(name) <> "monitortexture.jpg"
					If numb% = 0
						If MonitorTimer# < 50
							BrushTexture b, MonitorTexture2, 0, 0
						Else
							BrushTexture b, MonitorTexture4, 0, 0
						EndIf
					Else
						If MonitorTimer2# < 50
							BrushTexture b, MonitorTexture2, 0, 0
						Else
							BrushTexture b, MonitorTexture3, 0, 0
						EndIf
					EndIf
					PaintSurface sf,b
				EndIf
				If name<>"" Then FreeTexture t1
			EndIf
			FreeBrush b
		EndIf
	Next
	
End Function

Function TurnCheckpointMonitorsOff(numb%)
	Local i,sf,b,t1
	Local entity%
	
	If numb% = 0
		entity% = Monitor2
		UpdateCheckpoint1 = False
		MonitorTimer# = 0.0
	Else
		entity% = Monitor3
		UpdateCheckpoint2 = False
		MonitorTimer2# = 0.0
	EndIf
	
	For i = 2 To CountSurfaces(entity)
		sf = GetSurface(entity,i)
		b = GetSurfaceBrush(sf)
		If b<>0 Then
			t1 = GetBrushTexture(b,0)
			If t1<>0 Then
				name$ = StripPath(TextureName(t1))
				If Lower(name) <> "monitortexture.jpg"
					BrushTexture b, MonitorTextureOff, 0, 0
					PaintSurface sf,b
				EndIf
				If name<>"" Then FreeTexture t1
			EndIf
			FreeBrush b
		EndIf
	Next
	
End Function

Function TimeCheckpointMonitors()
	
	If UpdateCheckpoint1
		If MonitorTimer < 100.0
			MonitorTimer# = Min(MonitorTimer# + FPSfactor,100.0)
		Else
			MonitorTimer# = 0.0
		EndIf
	EndIf
	If UpdateCheckpoint2
		If MonitorTimer2 < 100.0
			MonitorTimer2# = Min(MonitorTimer2# + FPSfactor,100.0)
		Else
			MonitorTimer2# = 0.0
		EndIf
	EndIf
	
End Function

Function AmbientLightRooms(value%=0)
	Local mesh%,surf%,brush%,tex0%
	
	If value=AmbientLightRoomVal Then Return
	AmbientLightRoomVal = value
	
	Local oldbuffer% = BackBuffer() ;probably shouldn't make assumptions here but who cares, why wouldn't it use the backbuffer ;GetBuffer()
	
	SetBuffer TextureBuffer(AmbientLightRoomTex)
	
	ClsColor value,value,value
	Cls
	ClsColor 0,0,0
	
	SetBuffer oldbuffer
End Function

;TODO: clean up
Type ChunkPart
	Field Amount%
	Field obj%[128]
	Field RandomYaw#[128]
	Field ID
End Type

Function CreateChunkParts(r.Rooms)
	Local File$ = "Data\1499chunks.INI"
	Local ChunkAmount% = GetINIInt(File$,"general","count")
	Local i%,StrTemp$,j%
	Local chp.ChunkPart,chp2.ChunkPart
	Local obj%
	
	StrTemp$ = ""
	For i = 1 To Len(RandomSeed)
		StrTemp = StrTemp+Asc(Mid(RandomSeed,i,1))
	Next
	SeedRnd Abs(Int(StrTemp))
	
	For i = 0 To ChunkAmount%
		Local loc% = GetINISectionLocation(File$,"chunk"+i)
		If loc > 0 Then
			StrTemp$ = GetINIString2(File,loc%,"count")
			chp = New ChunkPart
			chp\Amount% = Int(StrTemp$)
			DebugLog "------------------"
			For j = 0 To Int(StrTemp$)
				Local objID% = GetINIString2(File$,loc%,"obj"+j)
				Local x$ = GetINIString2(File$,loc%,"obj"+j+"-x")
				Local z$ = GetINIString2(File$,loc%,"obj"+j+"-z")
				Local yaw$ = GetINIString2(File$,loc%,"obj"+j+"-yaw")
				DebugLog "1499 chunk X/Z/Yaw: "+x$+"|"+z$+"|"+yaw$
				chp\obj%[j] = CopyEntity(r\Objects[objID%])
				If Lower(yaw$) = "random"
					chp\RandomYaw#[j] = Rnd(360)
					RotateEntity chp\obj[j],0,chp\RandomYaw[j],0
				Else
					RotateEntity chp\obj[j],0,Float(yaw),0
				EndIf
				PositionEntity chp\obj[j],Float(x),0,Float(z)
				ScaleEntity chp\obj[j],RoomScale,RoomScale,RoomScale
				EntityType chp\obj[j],HIT_MAP
				EntityPickMode chp\obj[j],2
				;EntityParent chp\obj[j],r\obj
			Next
			chp2 = Before(chp)
			If chp2 <> Null
				chp\ID = chp2\ID+1
			EndIf
			DebugLog "<<<<<<<<<<<<<<<<"
			DebugLog "Generated 1499 chunk "+chp\ID+" sucessfully"
		EndIf
	Next
	
	SeedRnd MilliSecs()
	
End Function

Type Chunk
	Field obj%[128]
	Field x#,z#,y#
	Field Amount%
	;Field debugobj%
End Type

Function CreateChunk.Chunk(obj%,x#,y#,z#,spawnNPCs%=True)
	Local ch.Chunk = New Chunk
	Local chp.ChunkPart,i,n.NPCs
	
	;If obj%<>0
	;	ch\obj% = CopyEntity(obj%)
	;	PositionEntity ch\obj%,x,y,z
	;	ScaleEntity ch\obj%,RoomScale,RoomScale,RoomScale
	;	EntityType ch\obj%,HIT_MAP
	;EndIf
	
	;ch\debugobj% = CreateCube()
	;ScaleEntity ch\debugobj%,20,20,20
	;PositionEntity ch\debugobj%,x#,y#+20,z#
	;EntityColor ch\debugobj%,Rand(0,255),Rand(0,255),Rand(0,255)
	;EntityFX ch\debugobj%,1+FE_WIRE
	
	If obj% > -1
		For chp = Each ChunkPart
			If chp\ID = obj%
				ch\Amount% = chp\Amount%
				For i = 0 To chp\Amount
					ch\obj[i] = CopyEntity(chp\obj[i])
					PositionEntity ch\obj[i],x#,y#,z#
					;ScaleEntity ch\obj[i],RoomScale,RoomScale,RoomScale
					MoveEntity ch\obj[i],EntityX(chp\obj[i]),0,EntityZ(chp\obj[i])
				Next
				Exit
			EndIf
		Next
		If spawnNPCs%
		For i = 0 To Rand(5,10)
			n.NPCs = CreateNPC(NPCtype1499,x+Rnd(-60.0,60.0),y+0.5,z+Rnd(-60.0,60.0))
			If Rand(2)=1 Then n\State2 = 500*3
			n\Angle = Rnd(360)
		Next
	EndIf
	EndIf
	
	ch\x = x
	ch\z = z
	ch\y = y
	
	Return ch
End Function

Function UpdateChunks(r.Rooms,ChunkPartAmount%,spawnNPCs%=True)
	Local ch.Chunk, ch2.Chunk, chp.ChunkPart, ChunkPartAmount2%
	Local ChunkHideDistance% = 120
	Local temp% = False, temp2% = False
	Local x#,z#,i%,y#,CurrChunkX#,CurrChunkZ#
	Local obj%
	
	For ch = Each Chunk
		;If Distance(EntityX(Collider),EntityZ(Collider),ch\x,ch\z)<ChunkHideDistance
		;	;If ch\obj <> 0 Then ShowEntity ch\obj
		;	If ch\obj[0]<>0
		;		For i = 0 To ch\Amount
		;			ShowEntity ch\obj[i]
		;		Next
		;	EndIf
		;Else
		;	;If ch\obj <> 0 Then HideEntity ch\obj
		;	If ch\obj[0]<>0
		;		For i = 0 To ch\Amount
		;			HideEntity ch\obj[i]
		;		Next
		;	EndIf
		;EndIf
		If ch\obj[0]<>0
			For i = 0 To ch\Amount
				ShowEntity ch\obj[i]
			Next
		EndIf
		y# = ch\y
		If Abs(EntityX(Collider)-ch\x)<20
			If Abs(EntityZ(Collider)-ch\z)<20
				CurrChunkX# = ch\x
				CurrChunkZ# = ch\z
			EndIf
		EndIf
	Next
	
	;CurrChunkX# = Int(EntityX(Collider)/40)*40
	;CurrChunkZ# = Int(EntityZ(Collider)/40)*40
	
	x# = -(ChunkHideDistance+(CurrChunkX#))
	z# = -(ChunkHideDistance+(CurrChunkZ#))
	
	Local StrTemp$ = ""
	For i = 1 To Len(RandomSeed)
		StrTemp = StrTemp+Asc(Mid(RandomSeed,i,1))
	Next
	SeedRnd Abs(Int(StrTemp))
	
	Repeat
		temp2% = False
		For ch = Each Chunk
			If (ch\x=x#) And (ch\z=z#)
				temp2% = True
				Exit
			EndIf
		Next
		If (Not temp2%)
			;ch2 = CreateChunk(r\Objects[Rand(1,ChunkPartAmount%)],x#,y#,z#)
			ChunkPartAmount2 = GetINIInt("Data\1499chunks.INI","general","count")
			ch2 = CreateChunk(Rand(0,ChunkPartAmount2),x#,y#,z#,spawnNPCs%)
		EndIf
		If x# < (ChunkHideDistance+(CurrChunkX#))
			x# = x# + 40
		Else
			If z# < (ChunkHideDistance+(CurrChunkZ#))
				x# = -(ChunkHideDistance+(CurrChunkX#))
				z# = z# + 40
			Else
				Exit
			EndIf
		EndIf
	Forever
	
	SeedRnd MilliSecs()
	
End Function

Function HideChunks()
	Local ch.Chunk,i
	
	For ch = Each Chunk
		;If ch\obj <> 0 Then HideEntity ch\obj
		If ch\obj[0]<>0
			For i = 0 To ch\Amount
				HideEntity ch\obj[i]
			Next
		EndIf
	Next
	
End Function

Function DeleteChunks()
	
	Delete Each Chunk
	Delete Each ChunkPart
	
End Function

Type ElevatorObj
	Field obj%
	Field InFacility%
	Field door.Doors
End Type

Function AssignElevatorObj.ElevatorObj(obj%,door.Doors,in_facility%)
	Local eo.ElevatorObj = New ElevatorObj
	
	eo\obj% = obj%
	eo\door = door
	eo\InFacility% = in_facility%
	
	Return eo
End Function

Function DeleteElevatorObjects()
	
	Delete Each ElevatorObj
	
End Function

Function ValidRoom2slCamRoom(r.Rooms)
	If (r = Null) Then
		Return False
	EndIf
	
	Local RN$ = r\RoomTemplate\Name$
	
	If RN$ = "room2closets" Then Return True
	If RN$ = "room1archive" Then Return True
	If RN$ = "room3z3" Then Return True
	If RN$ = "room1lifts" Then Return True
	If RN$ = "room106" Then Return True
	If RN$ = "checkpoint1" Then Return True
	If RN$ = "room2nuke" Then Return True
	If RN$ = "008" Then Return True
	If RN$ = "room1162" Then Return True
	If RN$ = "room966" Then Return True
	If RN$ = "room2ccont" Then Return True
	
	Return False
	
End Function

Function FindAndDeleteFakeMonitor(r.Rooms,x#,y#,z#,Amount%)
	Local i%
	
	For i = 0 To Amount%
		If r\Objects[i]<>0
			If EntityX(r\Objects[i],True) = x#
				If EntityY(r\Objects[i],True) = y#
					If EntityZ(r\Objects[i],True) = z#
						FreeEntity r\Objects[i]
						r\Objects[i]=0
						DebugLog "Deleted Fake Monitor: "+i
						Exit
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
End Function
