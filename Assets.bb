Const ASSSET_NON%    = 0
Const ASSET_TEXTURE% = 1
Const ASSET_MESH%    = 2
Const ASSET_IMAGE%   = 3

Type AssetWrap
	Field asType%
	Field grabCount%
	Field file$
	Field intVal%
End Type

Function CreateAsset.AssetWrap(filePath$, asType%)
	Local as.AssetWrap = New AssetWrap
	as\asType = asType
	as\file = filePath

	Select as\asType
		Case ASSET_TEXTURE
			as\intVal = LoadTexture(as\file)
		Case ASSET_IMAGE
			as\intVal = LoadImage(as\file)
		Case ASSET_MESH
			as\intVal = LoadMesh(as\file)
	End Select
End Function

Function FreeAsset(as.AssetWrap)
	Select as\asType
		Case ASSET_TEXTURE
			FreeTexture(as\intVal)
		Case ASSET_IMAGE
			FreeImage(as\intVal)
		Case ASSET_MESH
			FreeEntity(as\intVal)
	End Select
	
	Delete as
End Function

Function GrabAsset%(filePath$, asType%)
	Local as.AssetWrap
	For as = Each AssetWrap
		If (filePath = as\file) Then
			as\grabCount = as\grabCount + 1
			Return as\intVal
		EndIf
	Next

	;Asset doesn't exist, create it.
	as = CreateAsset(filePath, asType)
	as\grabCount = 1

	Return as\intVal
End Function

Function DropAsset(filePath$)
	Local as.AssetWrap
	For as = Each AssetWrap
		If (filePath = as\file) Then
			as\grabCount = as\grabCount - 1
			Return
		EndIf
	Next

	;TODO: Maybe not make this crash the game later?
	RuntimeError("Attempted to drop non-existant asset: " + filePath)
End Function

Function UpdateAssets()
	Local as.AssetWrap
	For as = Each AssetWrap
		If (as\grabCount < 1) Then
			FreeAsset(as)
		EndIf
	Next
End Function

Const HAND_ICON_TOUCH% = 0
Const HAND_ICON_GRAB% = 1

Type UIAssets
	;Misc. Interface
	Field back%
	Field scpText%
	Field tileWhite%
	Field tileBlack%
	Field scp173%
	
	Field pauseMenuBG%
	
	Field cursorIMG%
	Field arrow%[4]
	
	Field font%[4]
	Field consoleFont%
	
	;HUD
	Field sprintIcon%
	Field blinkIcon%
	Field crouchIcon%
	Field handIcon%[2]
	Field blinkBar%
	Field staminaBar%
	Field keypadHUD%
	
End Type

Global uiAssets.UIAssets
Function InitializeUIAssets()
	uiAssets = New UIAssets
	
	uiAssets\back = LoadImage("GFX/menu/back.jpg")
	uiAssets\scpText = LoadImage("GFX/menu/scptext.jpg")
	uiAssets\scp173 = LoadImage("GFX/menu/173back.jpg")
	uiAssets\tileWhite = LoadImage("GFX/menu/menuwhite.jpg")
	uiAssets\tileBlack = LoadImage("GFX/menu/menublack.jpg")
	MaskImage uiAssets\tileBlack, 255,255,0
	
	ResizeImage(uiAssets\back, ImageWidth(uiAssets\back) * MenuScale, ImageHeight(uiAssets\back) * MenuScale)
	ResizeImage(uiAssets\scpText, ImageWidth(uiAssets\scpText) * MenuScale, ImageHeight(uiAssets\scpText) * MenuScale)
	ResizeImage(uiAssets\scp173, ImageWidth(uiAssets\scp173) * MenuScale, ImageHeight(uiAssets\scp173) * MenuScale)
	
	uiAssets\pauseMenuBG = LoadImage("GFX/menu/pausemenu.jpg")
	MaskImage uiAssets\pauseMenuBG, 255,255,0
	ScaleImage(uiAssets\pauseMenuBG, MenuScale, MenuScale)
	
	uiAssets\cursorIMG = LoadImage("GFX/cursor.png")
	
	Local i%
	For i = 0 To 3
		uiAssets\arrow[i] = LoadImage("GFX/menu/arrow.png")
		RotateImage(uiAssets\arrow[i], 90 * i)
		HandleImage(uiAssets\arrow[i], 0, 0)
	Next
	
	;For some reason, Blitz3D doesn't load fonts that have filenames that
	;don't match their "internal name" (i.e. their display name in applications
	;like Word and such). As a workaround, I moved the files and renamed them so they
	;can load without FastText.
	uiAssets\font[0] = LoadFont("GFX/font/cour/Courier New.ttf", Int(18 * MenuScale), 0,0,0)
	uiAssets\font[1] = LoadFont("GFX/font/courbd/Courier New.ttf", Int(58 * MenuScale), 0,0,0)
	uiAssets\font[2] = LoadFont("GFX/font/DS-DIGI/DS-Digital.ttf", Int(22 * MenuScale), 0,0,0)
	uiAssets\font[3] = LoadFont("GFX/font/DS-DIGI/DS-Digital.ttf", Int(60 * MenuScale), 0,0,0)
	uiAssets\consoleFont% = LoadFont("Blitz", Int(20 * MenuScale), 0,0,0)
	
	uiAssets\sprintIcon = LoadImage("GFX/sprinticon.png")
	uiAssets\blinkIcon% = LoadImage("GFX/blinkicon.png")
	uiAssets\crouchIcon% = LoadImage("GFX/sneakicon.png")
	uiAssets\handIcon[HAND_ICON_TOUCH] = LoadImage("GFX/handsymbol.png")
	uiAssets\handIcon[HAND_ICON_GRAB] = LoadImage("GFX/handsymbol2.png")
	uiAssets\blinkBar = LoadImage("GFX/blinkmeter.jpg")
	uiAssets\staminaBar = LoadImage("GFX/staminameter.jpg")
	uiAssets\keypadHUD = LoadImage("GFX/keypadhud.jpg")
	MaskImage(uiAssets\keypadHUD, 255,0,255)
End Function

Function ReleaseUIAssets()
	FreeImage(uiAssets\back)
	FreeImage(uiAssets\scpText)
	FreeImage(uiAssets\scp173)
	FreeImage(uiAssets\tileWhite)
	FreeImage(uiAssets\tileBlack)
	
	FreeImage(uiAssets\pauseMenuBG)
	
	FreeImage(uiAssets\cursorIMG)
	
	Local i%
	For i = 0 To 3
		FreeImage(uiAssets\arrow[i])
	Next
	
	For i = 0 To 3
		FreeFont(uiAssets\font[i])
	Next
	FreeFont(uiAssets\consoleFont)
	
	FreeImage(uiAssets\sprintIcon)
	FreeImage(uiAssets\blinkIcon)
	FreeImage(uiAssets\crouchIcon)
	For i = 0 To 1
		FreeImage(uiAssets\handIcon[i])
	Next
	FreeImage(uiAssets\blinkBar)
	FreeImage(uiAssets\staminaBar)
	FreeImage(uiAssets\keypadHUD)
	
	Delete uiAssets
End Function

Function LoadEntities()
	DrawLoading(0)
	
	Local i%
	
	;TODO: there may be a memory leak here,
	;probably gonna have to rework the tempsound system
	For i=0 To 9
		TempSounds[i]=0
	Next
	
	;TextureLodBias
	
	AmbientLightRoomTex% = CreateTexture(2,2,257)
	TextureBlend AmbientLightRoomTex,5
	SetBuffer(TextureBuffer(AmbientLightRoomTex))
	ClsColor 0,0,0
	Cls
	SetBuffer BackBuffer()
	AmbientLightRoomVal = 0
	
	SoundEmitter = CreatePivot()
	
	CreateBlurImage()
	CameraProjMode ark_blur_cam,0
	
	mainPlayer = CreatePlayer()
	
	AmbientLight(Brightness, Brightness, Brightness)
	
	ScreenTexs[0] = CreateTexture(512, 512, 1+256)
	ScreenTexs[1] = CreateTexture(512, 512, 1+256)
	
	;Listener = CreateListener(mainPlayer\cam)
	
	DrawLoading(5)
	TeslaTexture = LoadTexture("GFX/map/Textures/tesla.jpg", 1+2)
	
	LiquidObj = LoadMesh("GFX/items/cupliquid.x") ;optimized the cups dispensed by 294
	HideEntity LiquidObj
	
	MTFObj = LoadAnimMesh("GFX/npcs/MTF2.b3d") ;optimized MTFs
	;GuardTex = LoadTexture("GFX/npcs/body.jpg") ;optimized the guards even more
	
	;If BumpEnabled Then
	;	bump1 = LoadTexture("GFX/npcs/mtf_newnormal01.png")
	;	;TextureBlend bump1, FE_BUMP ;USE DOT3
	;		
	;	For i = 2 To CountSurfaces(MTFObj)
	;		sf = GetSurface(MTFObj,i)
	;		b = GetSurfaceBrush( sf )
	;		t1 = GetBrushTexture(b,0)
	;		
	;		Select Lower(StripPath(TextureName(t1)))
	;			Case "MTF_newdiffuse02.png"
	;				
	;				BrushTexture b, bump1, 0, 0
	;				BrushTexture b, t1, 0, 1
	;				PaintSurface sf,b
	;		End Select
	;		FreeBrush b
	;		FreeTexture t1
	;	Next
	;	FreeTexture bump1	
	;EndIf
	
	
	
	ClassDObj = LoadAnimMesh("GFX/npcs/classd.b3d") ;optimized Class-D's and scientists/researchers
	
	HideEntity MTFObj
	HideEntity ClassDObj
	
	LightSpriteTex(0) = LoadTexture("GFX/light1.jpg", 1)
	LightSpriteTex(1) = LoadTexture("GFX/light2.jpg", 1)
	LightSpriteTex(2) = LoadTexture("GFX/lightsprite.jpg",1)
	
	DrawLoading(10)
	
	DoorOBJ = LoadMesh("GFX/map/door01.x")
	HideEntity DoorOBJ
	DoorFrameOBJ = LoadMesh("GFX/map/doorframe.x")
	HideEntity DoorFrameOBJ
	
	HeavyDoorObj(0) = LoadMesh("GFX/map/heavydoor1.x")
	HideEntity HeavyDoorObj(0)
	HeavyDoorObj(1) = LoadMesh("GFX/map/heavydoor2.x")
	HideEntity HeavyDoorObj(1)
	
	DoorColl = LoadMesh("GFX/map/doorcoll.x")
	HideEntity DoorColl
	
	ButtonOBJ = LoadMesh("GFX/map/Button.x")
	HideEntity ButtonOBJ
	ButtonKeyOBJ = LoadMesh("GFX/map/ButtonKeycard.x")
	HideEntity ButtonKeyOBJ
	ButtonCodeOBJ = LoadMesh("GFX/map/ButtonCode.x")
	HideEntity ButtonCodeOBJ	
	ButtonScannerOBJ = LoadMesh("GFX/map/ButtonScanner.x")
	HideEntity ButtonScannerOBJ	
	
	BigDoorOBJ(0) = LoadMesh("GFX/map/ContDoorLeft.x")
	HideEntity BigDoorOBJ(0)
	BigDoorOBJ(1) = LoadMesh("GFX/map/ContDoorRight.x")
	HideEntity BigDoorOBJ(1)
	
	LeverBaseOBJ = LoadMesh("GFX/map/leverbase.x")
	HideEntity LeverBaseOBJ
	LeverOBJ = LoadMesh("GFX/map/leverhandle.x")
	HideEntity LeverOBJ
	
	;For i = 0 To 1
	;	HideEntity BigDoorOBJ(i)
	;	;If BumpEnabled And 0 Then
	;	If BumpEnabled
	;		
	;		Local bumptex = LoadTexture("GFX/map/containmentdoorsbump.jpg")
	;		;TextureBlend bumptex, FE_BUMP
	;		Local tex = LoadTexture("GFX/map/containment_doors.jpg")	
	;		EntityTexture BigDoorOBJ(i), bumptex, 0, 0
	;		EntityTexture BigDoorOBJ(i), tex, 0, 1
	;		
	;		;FreeEntity tex
	;		;FreeEntity bumptex
	;		FreeTexture tex
	;		FreeTexture bumptex
	;	EndIf
	;Next
	
	DrawLoading(15)
	
	For i = 0 To 5
		GorePics(i) = LoadTexture("GFX/895pics/pic" + (i + 1) + ".jpg")
	Next
	
	OldAiPics(0) = LoadTexture("GFX/AIface.jpg")
	OldAiPics(1) = LoadTexture("GFX/AIface2.jpg")	
	
	DrawLoading(20)
	
	For i = 0 To 6
		DecalTextures(i) = LoadTexture("GFX/decal" + (i + 1) + ".png", 1 + 2)
	Next
	DecalTextures(7) = LoadTexture("GFX/items/INVpaperstrips.jpg", 1 + 2)
	For i = 8 To 12
		DecalTextures(i) = LoadTexture("GFX/decalpd"+(i-7)+".jpg", 1 + 2)	
	Next
	For i = 13 To 14
		DecalTextures(i) = LoadTexture("GFX/bullethole"+(i-12)+".jpg", 1 + 2)	
	Next	
	For i = 15 To 16
		DecalTextures(i) = LoadTexture("GFX/blooddrop"+(i-14)+".png", 1 + 2)	
	Next
	DecalTextures(17) = LoadTexture("GFX/decal8.png", 1 + 2)	
	DecalTextures(18) = LoadTexture("GFX/decalpd6.dc", 1 + 2)	
	DecalTextures(19) = LoadTexture("GFX/decal19.png", 1 + 2)
	
	DrawLoading(25)
	
	Monitor = LoadMesh("GFX/map/monitor.b3d")
	HideEntity Monitor
	MonitorTexture = LoadTexture("GFX/monitortexture.jpg")
	
	CamBaseOBJ = LoadMesh("GFX/map/cambase.x")
	HideEntity(CamBaseOBJ)
	CamOBJ = LoadMesh("GFX/map/CamHead.b3d")
	HideEntity(CamOBJ)
	
	Monitor2 = LoadMesh("GFX/map/monitor_checkpoint.b3d")
	HideEntity Monitor2
	Monitor3 = LoadMesh("GFX/map/monitor_checkpoint.b3d")
	HideEntity Monitor3
	MonitorTexture2 = LoadTexture("GFX/map/LockdownScreen2.jpg")
	MonitorTexture3 = LoadTexture("GFX/map/LockdownScreen.jpg")
	MonitorTexture4 = LoadTexture("GFX/map/LockdownScreen3.jpg")
	MonitorTextureOff = CreateTexture(1,1)
	SetBuffer TextureBuffer(MonitorTextureOff)
	ClsColor 0,0,0
	Cls
	SetBuffer BackBuffer()
	
	For i = 2 To CountSurfaces(Monitor2)
		sf = GetSurface(Monitor2,i)
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
	For i = 2 To CountSurfaces(Monitor3)
		sf = GetSurface(Monitor3,i)
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
	
	InitItemTemplates()
	
	ParticleTextures(0) = LoadTexture("GFX/smoke.png", 1 + 2)
	ParticleTextures(1) = LoadTexture("GFX/flash.jpg", 1 + 2)
	ParticleTextures(2) = LoadTexture("GFX/dust.jpg", 1 + 2)
	ParticleTextures(3) = LoadTexture("GFX/npcs/hg.pt", 1 + 2)
	ParticleTextures(4) = LoadTexture("GFX/map/sun.jpg", 1 + 2)
	ParticleTextures(5) = LoadTexture("GFX/bloodsprite.png", 1 + 2)
	ParticleTextures(6) = LoadTexture("GFX/smoke2.png", 1 + 2)
	ParticleTextures(7) = LoadTexture("GFX/spark.jpg", 1 + 2)
	
	LoadMaterials("DATA/materials.ini")
	
	;TextureLodBias TextureFloat#
	
	DrawLoading(30)
	
	LoadRoomTemplates("Data/rooms.ini")
	
	;LoadRoomMeshes()
	
End Function

Function InitNewGame()
	Local i%, de.Decals, d.Doors, it.Items, r.Rooms, sc.SecurityCams, e.Events
	
	DrawLoading(45)
	
	LoadInGameSounds(sndManager)
	
	HideDistance# = 15.0
	
	mainPlayer\heartbeatIntensity = 70
	;HeartBeatRate = 70
	
	AccessCode = 0
	For i = 0 To 3
		AccessCode = AccessCode + Rand(1,9)*(10^i)
	Next	
	
	If SelectedMap = "" Then
		CreateMap()
	Else
		LoadMap("Map Creator/Maps/"+SelectedMap)
	EndIf
	InitWayPoints()
	
	DrawLoading(79)
	
	Curr173 = CreateNPC(NPCtype173, 0, -30.0, 0)
	Curr106 = CreateNPC(NPCtype106, 0, -30.0, 0)
	Curr106\state = 70 * 60 * Rand(12,17)
	
	For d.Doors = Each Doors
		EntityParent(d\obj, 0)
		If d\obj2 > 0 Then EntityParent(d\obj2, 0)
		If d\frameobj > 0 Then EntityParent(d\frameobj, 0)
		If d\buttons[0] > 0 Then EntityParent(d\buttons[0], 0)
		If d\buttons[1] > 0 Then EntityParent(d\buttons[1], 0)
		
		If d\obj2 <> 0 And d\dir = 0 Then
			MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
			MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
		EndIf	
	Next
	
	For it.Items = Each Items
		EntityType (it\collider, HIT_ITEM)
		EntityParent(it\collider, 0)
	Next
	
	DrawLoading(80)
	For sc.SecurityCams= Each SecurityCams
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next	
	
	For r.Rooms = Each Rooms
		For i = 0 To MaxRoomLights
			If r\Lights[i]<>0 Then EntityParent(r\Lights[i],0)
		Next
		
		If (Not r\RoomTemplate\DisableDecals) Then
			If Rand(4) = 1 Then
				de.Decals = CreateDecal(Rand(2, 3), EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\Size = Rnd(0.1, 0.4) : ScaleSprite(de\obj, de\Size, de\Size)
				EntityAlpha(de\obj, Rnd(0.85, 0.95))
			EndIf
			
			If Rand(4) = 1 Then
				de.Decals = CreateDecal(0, EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\Size = Rnd(0.5, 0.7) : EntityAlpha(de\obj, 0.7) : de\ID = 1 : ScaleSprite(de\obj, de\Size, de\Size)
				EntityAlpha(de\obj, Rnd(0.7, 0.85))
			EndIf
		EndIf
		
		If (r\RoomTemplate\name = "cont_173_1" And (userOptions\introEnabled = False)) Then 
			PositionEntity (mainPlayer\collider, EntityX(r\obj)+3584*RoomScale, 714*RoomScale, EntityZ(r\obj)+1024*RoomScale)
			ResetEntity mainPlayer\collider
			mainPlayer\currRoom = r
		ElseIf (r\RoomTemplate\name = "intro" And userOptions\introEnabled) Then
			PositionEntity (mainPlayer\collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
			ResetEntity mainPlayer\collider
			mainPlayer\currRoom = r
		EndIf
		
	Next
	
	Local rt.RoomTemplates
	For rt.RoomTemplates = Each RoomTemplates
		If rt\collisionObjs<>Null Then
			For i% = 0 To rt\collisionObjs\size-1
				FreeEntity GetIntArrayListElem(rt\collisionObjs,i)
			Next
			DeleteIntArrayList(rt\collisionObjs) : rt\collisionObjs = Null
		EndIf
		
		FreeEntity rt\opaqueMesh
		If rt\alphaMesh<>0 Then FreeEntity rt\alphaMesh
		
		Local prop.Props
		If rt\props<>Null Then
			For i% = 0 To rt\props\size-1
				prop = Object.Props(GetIntArrayListElem(rt\props,i))
				FreeEntity(prop\obj)
				Delete prop
			Next
			DeleteIntArrayList(rt\props) : rt\props = Null
		EndIf
	Next
	
	Local tw.TempWayPoints
	For tw.TempWayPoints = Each TempWayPoints
		Delete tw
	Next
	
	TurnEntity(mainPlayer\collider, 0, Rand(160, 200), 0)
	
	ResetEntity mainPlayer\collider
	
	If SelectedMap = "" Then InitEvents()
	
	;TODO: fix
;	For e.Events = Each Events
;		If e\EventName = "room2nuke"
;			e\EventState = 1
;			DebugLog "room2nuke"
;		EndIf
;		If e\EventName = "room106"
;			e\EventState2 = 1
;			DebugLog "room106"
;		EndIf	
;		If e\EventName = "room2sl"
;			e\EventState3 = 1
;			DebugLog "room2sl"
;		EndIf
;	Next
	
	MoveMouse viewport_center_x,viewport_center_y;320, 240
	
	SetFont uiAssets\font[0]
	
	HidePointer()
	
	mainPlayer\blinkTimer = -10
	mainPlayer\blurTimer = 100
	mainPlayer\stamina = 100
	
	For i% = 0 To 70
		FlushKeys()
		MovePlayer()
		UpdateRooms()
		UpdateDoors()
		UpdateNPCs()
		UpdateWorld()
		;Cls
		If (Int(Float(i)*0.27)<>Int(Float(i-1)*0.27)) Then
			DrawLoading(80+Int(Float(i)*0.27))
		EndIf
	Next
	
	FreeTextureCache
	DrawLoading(100)
	
	FlushKeys
	FlushMouse
	
	mainPlayer\dropSpeed = 0
End Function

Function InitLoadGame()
	Local d.Doors, sc.SecurityCams, rt.RoomTemplates, e.Events
	
	DrawLoading(80)
	
	For d.Doors = Each Doors
		EntityParent(d\obj, 0)
		If d\obj2 > 0 Then EntityParent(d\obj2, 0)
		If d\frameobj > 0 Then EntityParent(d\frameobj, 0)
		If d\buttons[0] > 0 Then EntityParent(d\buttons[0], 0)
		If d\buttons[1] > 0 Then EntityParent(d\buttons[1], 0)
		
	Next
	
	For sc.SecurityCams = Each SecurityCams
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next
	
	ResetEntity mainPlayer\collider
	
	;InitEvents()
	
	DrawLoading(90)
	
	MoveMouse viewport_center_x,viewport_center_y
	
	SetFont uiAssets\font[0]
	
	HidePointer ()
	
	mainPlayer\blinkTimer = mainPlayer\blinkFreq
	mainPlayer\stamina = 100
	
	For rt.RoomTemplates = Each RoomTemplates
		If rt\collisionObjs<>Null Then
			For i% = 0 To rt\collisionObjs\size-1
				FreeEntity GetIntArrayListElem(rt\collisionObjs,i)
			Next
			DeleteIntArrayList(rt\collisionObjs) : rt\collisionObjs = Null
		EndIf
		
		FreeEntity rt\opaqueMesh
		If rt\alphaMesh<>0 Then FreeEntity rt\alphaMesh
		
		Local prop.Props
		If rt\props<>Null Then
			For i% = 0 To rt\props\size-1
				prop = Object.Props(GetIntArrayListElem(rt\props,i))
				FreeEntity(prop\obj)
				Delete prop
			Next
			DeleteIntArrayList(rt\props) : rt\props = Null
		EndIf
	Next
	
	mainPlayer\dropSpeed = 0.0
	
	For e.Events = Each Events
		;Loading the necessary stuff for dimension1499, but this will only be done if the player is in this dimension already
		If e\EventName = "dimension1499"
			If e\EventState = 2
				;[Block]
				DrawLoading(91)
				e\room\Objects[0] = CreatePlane()
				Local planetex% = LoadTexture("GFX/map/Rooms/dimension1499/grit3.jpg")
				EntityTexture e\room\Objects[0],planetex%
				FreeTexture planetex%
				PositionEntity e\room\Objects[0],0,EntityY(e\room\obj),0
				EntityType e\room\Objects[0],HIT_MAP
				;EntityParent e\room\Objects[0],e\room\obj
				DrawLoading(92)
				NTF_1499Sky = sky_CreateSky("GFX/map/sky/1499sky")
				DrawLoading(93)
				For i = 1 To 15
					e\room\Objects[i] = LoadMesh("GFX/map/Rooms/dimension1499/1499object"+i+".b3d")
					HideEntity e\room\Objects[i]
				Next
				DrawLoading(96)
				CreateChunkParts(e\room)
				DrawLoading(97)
				x# = EntityX(e\room\obj)
				z# = EntityZ(e\room\obj)
				Local ch.Chunk
				For i = -2 To 2 Step 2
					ch = CreateChunk(-1,x#*(i*2.5),EntityY(e\room\obj),z#)
				Next
				;If Music(18)=0 Then Music(18) = LoadSound("SFX/Music/1499.ogg") ;TODO: fix
				DrawLoading(98)
				UpdateChunks(e\room,15,False)
				;MoveEntity mainPlayer\collider,0,10,0
				;ResetEntity mainPlayer\collider
				
				DebugLog "Loaded dimension1499 successful"
				
				Exit
				;[End Block]
			EndIf
		EndIf
	Next
	
	FreeTextureCache
	
	DrawLoading(100)
End Function

Function NullGame()
	Local i%, x%, y%, lvl
	Local itt.ItemTemplates, s.Screens, lt.LightTemplates, d.Doors, m.Materials
	Local wp.WayPoints, twp.TempWayPoints, r.Rooms, it.Items
	
	DeloadInGameSounds(sndManager)
	
	ClearTextureCache()
	
	DeletePlayer(mainPlayer) : mainPlayer = Null
	
	DeathMSG$=""
	
	SelectedMap = ""
	
	DoorTempID = 0
	
	GameSaved = 0
	
	HideDistance# = 15.0
	
	For itt.ItemTemplates = Each ItemTemplates
		itt\found = False
	Next
	
	Contained106 = False
	Curr173\Idle = False
	
	MTFtimer = 0
	For i = 0 To 9
		MTFrooms[i]=Null
		MTFroomState[i]=0
	Next
	
	For s.Screens = Each Screens
		If s\img <> 0 Then FreeImage s\img : s\img = 0
		Delete s
	Next

	;RefinedItems = 0 ;TODO: reimplement?
	
	ConsoleInput = ""
	;ConsoleOpen = False
	
	;TODO: fix
	;EyeIrritation = 0
	;EyeStuck = 0
	
	Msg = ""
	MsgTimer = 0
	
	For d.Doors = Each Doors
		Delete d
	Next
	
	;ClearWorld
	
	For lt.LightTemplates = Each LightTemplates
		Delete lt
	Next 
	
	For m.Materials = Each Materials
		Delete m
	Next
	
	For wp.WayPoints = Each WayPoints
		Delete wp
	Next
	
	For twp.TempWayPoints = Each TempWayPoints
		Delete twp
	Next	
	
	For r.Rooms = Each Rooms
		DeleteIntArray(r\collisionObjs)
		If r\props<>Null Then DeleteIntArray(r\props)
		Delete r
	Next
	DeleteIntArray(MapRooms)
	
	For rt.RoomTemplates = Each RoomTemplates
		Delete rt
	Next
	
	For itt.ItemTemplates = Each ItemTemplates
		Delete itt
	Next 
	
	For it.Items = Each Items
		Delete it
	Next
	
	For pr.Props = Each Props
		Delete pr
	Next
	
	For de.decals = Each Decals
		Delete de
	Next
	
	For n.NPCS = Each NPCs
		Delete n
	Next
	Curr173 = Null
	Curr106 = Null
	Curr096 = Null
	For i = 0 To 6
		MTFrooms[i]=Null
	Next
	
	Local e.Events
	For e.Events = Each Events
		If e\sounds[0]<>0 Then FreeSound e\sounds[0]
		If e\sounds[1]<>0 Then FreeSound e\sounds[1]
		Delete e
	Next
	
	For sc.securitycams = Each SecurityCams
		Delete sc
	Next
	
	For em.emitters = Each Emitters
		Delete em
	Next	
	
	For p.particles = Each Particles
		Delete p
	Next
	
	For i = 0 To 5
		If IsChannelPlaying(RadioCHN(i)) Then StopChannel(RadioCHN(i))
	Next
	
	NTF_1499PrevX# = 0.0
	NTF_1499PrevY# = 0.0
	NTF_1499PrevZ# = 0.0
	NTF_1499PrevRoom = Null
	NTF_1499X# = 0.0
	NTF_1499Y# = 0.0
	NTF_1499Z# = 0.0
	DeleteChunks()
	
	DeleteElevatorObjects()
	
	NoTarget% = False
	
	;OptionsMenu% = -1
	;QuitMSG% = -1
	
	IsZombie% = False
	
	;DeInitExt
	
	ClearWorld
	ark_blur_cam = 0
	InitFastResize()
	
	For i=0 To 9
		If TempSounds[i]<>0 Then FreeSound(TempSounds[i]) : TempSounds[i]=0
	Next
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D