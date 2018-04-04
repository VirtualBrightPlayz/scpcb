Function LoadEntities()
	DrawLoading(0)
	
	Local i%
	
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
	
	mainPlayer = CreatePlayer()
	
	AmbientLight(Brightness, Brightness, Brightness)
	
	ScreenTexs[0] = CreateTexture(512, 512, 1+256)
	ScreenTexs[1] = CreateTexture(512, 512, 1+256)
	
	CreateBlurImage()
	CameraProjMode ark_blur_cam,0
	;Listener = CreateListener(mainPlayer\cam)
	
	DrawLoading(5)
	TeslaTexture = LoadTexture_Strict("GFX\map\tesla.jpg", 1+2)
	
	LiquidObj = LoadMesh_Strict("GFX\items\cupliquid.x") ;optimized the cups dispensed by 294
	HideEntity LiquidObj
	
	MTFObj = LoadAnimMesh_Strict("GFX\npcs\MTF2.b3d") ;optimized MTFs
	;GuardTex = LoadTexture_Strict("GFX\npcs\body.jpg") ;optimized the guards even more
	
	;If BumpEnabled Then
	;	bump1 = LoadTexture_Strict("GFX\npcs\mtf_newnormal01.png")
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
	
	
	
	ClassDObj = LoadAnimMesh_Strict("GFX\npcs\classd.b3d") ;optimized Class-D's and scientists/researchers
	ApacheObj = LoadAnimMesh_Strict("GFX\apache.b3d") ;optimized Apaches (helicopters)
	ApacheRotorObj = LoadAnimMesh_Strict("GFX\apacherotor.b3d") ;optimized the Apaches even more
	
	HideEntity MTFObj
	HideEntity ClassDObj
	HideEntity ApacheObj
	HideEntity ApacheRotorObj
	
	LightSpriteTex(0) = LoadTexture_Strict("GFX\light1.jpg", 1)
	LightSpriteTex(1) = LoadTexture_Strict("GFX\light2.jpg", 1)
	LightSpriteTex(2) = LoadTexture_Strict("GFX\lightsprite.jpg",1)
	
	DrawLoading(10)
	
	DoorOBJ = LoadMesh_Strict("GFX\map\door01.x")
	HideEntity DoorOBJ
	DoorFrameOBJ = LoadMesh_Strict("GFX\map\doorframe.x")
	HideEntity DoorFrameOBJ
	
	HeavyDoorObj(0) = LoadMesh_Strict("GFX\map\heavydoor1.x")
	HideEntity HeavyDoorObj(0)
	HeavyDoorObj(1) = LoadMesh_Strict("GFX\map\heavydoor2.x")
	HideEntity HeavyDoorObj(1)
	
	DoorColl = LoadMesh_Strict("GFX\map\doorcoll.x")
	HideEntity DoorColl
	
	ButtonOBJ = LoadMesh_Strict("GFX\map\Button.x")
	HideEntity ButtonOBJ
	ButtonKeyOBJ = LoadMesh_Strict("GFX\map\ButtonKeycard.x")
	HideEntity ButtonKeyOBJ
	ButtonCodeOBJ = LoadMesh_Strict("GFX\map\ButtonCode.x")
	HideEntity ButtonCodeOBJ	
	ButtonScannerOBJ = LoadMesh_Strict("GFX\map\ButtonScanner.x")
	HideEntity ButtonScannerOBJ	
	
	BigDoorOBJ(0) = LoadMesh_Strict("GFX\map\ContDoorLeft.x")
	HideEntity BigDoorOBJ(0)
	BigDoorOBJ(1) = LoadMesh_Strict("GFX\map\ContDoorRight.x")
	HideEntity BigDoorOBJ(1)
	
	LeverBaseOBJ = LoadMesh_Strict("GFX\map\leverbase.x")
	HideEntity LeverBaseOBJ
	LeverOBJ = LoadMesh_Strict("GFX\map\leverhandle.x")
	HideEntity LeverOBJ
	
	;For i = 0 To 1
	;	HideEntity BigDoorOBJ(i)
	;	;If BumpEnabled And 0 Then
	;	If BumpEnabled
	;		
	;		Local bumptex = LoadTexture_Strict("GFX\map\containmentdoorsbump.jpg")
	;		;TextureBlend bumptex, FE_BUMP
	;		Local tex = LoadTexture_Strict("GFX\map\containment_doors.jpg")	
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
		GorePics(i) = LoadTexture_Strict("GFX\895pics\pic" + (i + 1) + ".jpg")
	Next
	
	OldAiPics(0) = LoadTexture_Strict("GFX\AIface.jpg")
	OldAiPics(1) = LoadTexture_Strict("GFX\AIface2.jpg")	
	
	DrawLoading(20)
	
	For i = 0 To 6
		DecalTextures(i) = LoadTexture_Strict("GFX\decal" + (i + 1) + ".png", 1 + 2)
	Next
	DecalTextures(7) = LoadTexture_Strict("GFX\items\INVpaperstrips.jpg", 1 + 2)
	For i = 8 To 12
		DecalTextures(i) = LoadTexture_Strict("GFX\decalpd"+(i-7)+".jpg", 1 + 2)	
	Next
	For i = 13 To 14
		DecalTextures(i) = LoadTexture_Strict("GFX\bullethole"+(i-12)+".jpg", 1 + 2)	
	Next	
	For i = 15 To 16
		DecalTextures(i) = LoadTexture_Strict("GFX\blooddrop"+(i-14)+".png", 1 + 2)	
	Next
	DecalTextures(17) = LoadTexture_Strict("GFX\decal8.png", 1 + 2)	
	DecalTextures(18) = LoadTexture_Strict("GFX\decalpd6.dc", 1 + 2)	
	DecalTextures(19) = LoadTexture_Strict("GFX\decal19.png", 1 + 2)
	
	DrawLoading(25)
	
	Monitor = LoadMesh_Strict("GFX\map\monitor.b3d")
	HideEntity Monitor
	MonitorTexture = LoadTexture_Strict("GFX\monitortexture.jpg")
	
	CamBaseOBJ = LoadMesh_Strict("GFX\map\cambase.x")
	HideEntity(CamBaseOBJ)
	CamOBJ = LoadMesh_Strict("GFX\map\CamHead.b3d")
	HideEntity(CamOBJ)
	
	Monitor2 = LoadMesh_Strict("GFX\map\monitor_checkpoint.b3d")
	HideEntity Monitor2
	Monitor3 = LoadMesh_Strict("GFX\map\monitor_checkpoint.b3d")
	HideEntity Monitor3
	MonitorTexture2 = LoadTexture_Strict("GFX\map\LockdownScreen2.jpg")
	MonitorTexture3 = LoadTexture_Strict("GFX\map\LockdownScreen.jpg")
	MonitorTexture4 = LoadTexture_Strict("GFX\map\LockdownScreen3.jpg")
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
	
	ParticleTextures(0) = LoadTexture_Strict("GFX\smoke.png", 1 + 2)
	ParticleTextures(1) = LoadTexture_Strict("GFX\flash.jpg", 1 + 2)
	ParticleTextures(2) = LoadTexture_Strict("GFX\dust.jpg", 1 + 2)
	ParticleTextures(3) = LoadTexture_Strict("GFX\npcs\hg.pt", 1 + 2)
	ParticleTextures(4) = LoadTexture_Strict("GFX\map\sun.jpg", 1 + 2)
	ParticleTextures(5) = LoadTexture_Strict("GFX\bloodsprite.png", 1 + 2)
	ParticleTextures(6) = LoadTexture_Strict("GFX\smoke2.png", 1 + 2)
	ParticleTextures(7) = LoadTexture_Strict("GFX\spark.jpg", 1 + 2)
	
	LoadMaterials("DATA\materials.ini")
	
	;TextureLodBias TextureFloat#
	
	DrawLoading(30)
	
	;LoadRoomMeshes()
	
End Function

Function InitNewGame()
	Local i%, de.Decals, d.Doors, it.Items, r.Rooms, sc.SecurityCams, e.Events
	
	DrawLoading(45)
	
	HideDistance# = 15.0
	
	HeartBeatRate = 70
	
	AccessCode = 0
	For i = 0 To 3
		AccessCode = AccessCode + Rand(1,9)*(10^i)
	Next	
	
	If SelectedMap = "" Then
		CreateMap()
	Else
		LoadMap("Map Creator\Maps\"+SelectedMap)
	EndIf
	InitWayPoints()
	
	DrawLoading(79)
	
	Curr173 = CreateNPC(NPCtype173, 0, -30.0, 0)
	Curr106 = CreateNPC(NPCtype106, 0, -30.0, 0)
	Curr106\State = 70 * 60 * Rand(12,17)
	
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
		
		If (r\RoomTemplate\Name = "start" And userOptions\introEnabled = False) Then 
			PositionEntity (mainPlayer\collider, EntityX(r\obj)+3584*RoomScale, 704*RoomScale, EntityZ(r\obj)+1024*RoomScale)
			mainPlayer\currRoom = r
		ElseIf (r\RoomTemplate\Name = "173" And userOptions\introEnabled) Then
			PositionEntity (mainPlayer\collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
			mainPlayer\currRoom = r
		EndIf
		
	Next
	
	Local rt.RoomTemplates
	For rt.RoomTemplates = Each RoomTemplates
		FreeEntity (rt\obj)
	Next	
	
	Local tw.TempWayPoints
	For tw.TempWayPoints = Each TempWayPoints
		Delete tw
	Next
	
	TurnEntity(mainPlayer\collider, 0, Rand(160, 200), 0)
	
	ResetEntity mainPlayer\collider
	
	If SelectedMap = "" Then InitEvents()
	
	For e.Events = Each Events
		If e\EventName = "room2nuke"
			e\EventState = 1
			DebugLog "room2nuke"
		EndIf
		If e\EventName = "room106"
			e\EventState2 = 1
			DebugLog "room106"
		EndIf	
		If e\EventName = "room2sl"
			e\EventState3 = 1
			DebugLog "room2sl"
		EndIf
	Next
	
	MoveMouse viewport_center_x,viewport_center_y;320, 240
	
	SetFont Font1
	
	HidePointer()
	
	mainPlayer\blinkTimer = -10
	mainPlayer\blurTimer = 100
	mainPlayer\stamina = 100
	
	For i% = 0 To 70
		FPSfactor = 1.0
		FlushKeys()
		MovePlayer()
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
	
	PrevTime = MilliSecs()
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
	
	SetFont Font1
	
	HidePointer ()
	
	mainPlayer\blinkTimer = BLINKFREQ
	mainPlayer\stamina = 100
	
	For rt.RoomTemplates = Each RoomTemplates
		If rt\obj <> 0 Then FreeEntity(rt\obj) : rt\obj = 0
	Next
	
	mainPlayer\dropSpeed = 0.0
	
	For e.Events = Each Events
		;Loading the necessary stuff for dimension1499, but this will only be done if the player is in this dimension already
		If e\EventName = "dimension1499"
			If e\EventState = 2
				;[Block]
				DrawLoading(91)
				e\room\Objects[0] = CreatePlane()
				Local planetex% = LoadTexture_Strict("GFX\map\dimension1499\grit3.jpg")
				EntityTexture e\room\Objects[0],planetex%
				FreeTexture planetex%
				PositionEntity e\room\Objects[0],0,EntityY(e\room\obj),0
				EntityType e\room\Objects[0],HIT_MAP
				;EntityParent e\room\Objects[0],e\room\obj
				DrawLoading(92)
				NTF_1499Sky = sky_CreateSky("GFX\map\sky\1499sky")
				DrawLoading(93)
				For i = 1 To 15
					e\room\Objects[i] = LoadMesh_Strict("GFX\map\dimension1499\1499object"+i+".b3d")
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
				If Music(18)=0 Then Music(18) = LoadSound_Strict("SFX\Music\1499.ogg")
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
	
	PrevTime = MilliSecs()
	FPSfactor = 0	
End Function

Function NullGame()
	Local i%, x%, y%, lvl
	Local itt.ItemTemplates, s.Screens, lt.LightTemplates, d.Doors, m.Materials
	Local wp.WayPoints, twp.TempWayPoints, r.Rooms, it.Items
	
	ClearTextureCache()
	
	DeletePlayer(mainPlayer) : mainPlayer = Null
	
	UnableToMove% = False
	
	QuickLoadPercent = -1
	QuickLoadPercent_DisplayTimer# = 0
	
	DeathMSG$=""
	
	SelectedMap = ""
	
	UsedConsole = False
	
	DoorTempID = 0
	RoomTempID = 0
	
	GameSaved = 0
	
	HideDistance# = 15.0
	
	For lvl = 0 To 0
		For x = 0 To MapWidth - 1
			For y = 0 To MapHeight - 1
				MapTemp(x, y) = 0
				MapFound(x, y) = 0
			Next
		Next
	Next
	
	For itt.ItemTemplates = Each ItemTemplates
		itt\found = False
	Next
	
	mainPlayer\dropSpeed = 0
	Shake = 0
	mainPlayer\moveSpeed = 0
	
	DeathTimer=0
	
	HeartBeatVolume = 0
	
	StaminaEffect = 1.0
	StaminaEffectTimer = 0
	BlinkEffect = 1.0
	BlinkEffectTimer = 0
	
	Bloodloss = 0
	mainPlayer\injuries = 0
	Infect = 0
	
	SelectedEnding = ""
	EndingTimer = 0
	ExplosionTimer = 0
	
	mainPlayer\camShake = 0
	Shake = 0
	mainPlayer\lightFlash = 0
	
	GodMode = 0
	NoClip = 0
	WireframeState = 0
	WireFrame 0
	If WearingNightVision Then
		CameraFogFar = StoredCameraFogFar
		WearingNightVision = 0
	EndIf
	
	ForceMove = 0.0
	ForceAngle = 0.0	
	Playable = True
	
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
	
	For i = 0 To MAXACHIEVEMENTS-1
		Achievements(i)=0
	Next
	RefinedItems = 0
	
	ConsoleInput = ""
	ConsoleOpen = False
	
	EyeIrritation = 0
	EyeStuck = 0
	
	ShouldPlay = 0
	
	KillTimer = 0
	FallTimer = 0
	mainPlayer\stamina = 100
	mainPlayer\blurTimer = 0
	SuperMan = False
	SuperManTimer = 0
	
	InfiniteStamina% = False
	
	Msg = ""
	MsgTimer = 0
	
	mainPlayer\selectedItem = Null
	
	For i = 0 To MaxItemAmount - 1
		Inventory(i) = Null
	Next
	mainPlayer\selectedItem = Null
	
	mainPlayer\closestButton = 0
	
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
		Delete r
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
		If e\Sound<>0 Then FreeSound_Strict e\Sound
		If e\Sound2<>0 Then FreeSound_Strict e\Sound2
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
	
	For rt.RoomTemplates = Each RoomTemplates
		rt\obj = 0
	Next
	
	For i = 0 To 5
		If ChannelPlaying(RadioCHN(i)) Then StopChannel(RadioCHN(i))
	Next
	
	NTF_1499PrevX# = 0.0
	NTF_1499PrevY# = 0.0
	NTF_1499PrevZ# = 0.0
	NTF_1499PrevRoom = Null
	NTF_1499X# = 0.0
	NTF_1499Y# = 0.0
	NTF_1499Z# = 0.0
	Wearing1499% = False
	DeleteChunks()
	
	DeleteElevatorObjects()
	
	NoTarget% = False
	
	OptionsMenu% = -1
	QuitMSG% = -1
	AchievementsMenu% = -1
	
	IsZombie% = False
	
	;DeInitExt
	
	ClearWorld
	mainPlayer\cam = 0
	ark_blur_cam = 0
	InitFastResize()
	
	For i=0 To 9
		If TempSounds[i]<>0 Then FreeSound_Strict TempSounds[i] : TempSounds[i]=0
	Next
	
End Function