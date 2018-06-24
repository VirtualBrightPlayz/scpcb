Const ASSET_NONE%       = 0
Const ASSET_TEXTURE%    = 1
Const ASSET_IMAGE%      = 2
Const ASSET_MESH%       = 3
Const ASSET_ANIM_MESH%  = 4

Const ASSET_DECAY_TIMER% = 10 * 70

Const BLEND_ADD% = 3

Type AssetWrap
	Field asType%
	Field grabCount%
	Field file$
	Field decayTimer#
	Field intVal%
End Type

Function CreateAsset.AssetWrap(filePath$, asType%, flag%=1)
	Local as.AssetWrap = New AssetWrap
	as\asType = asType
	as\file = filePath
	as\decayTimer = ASSET_DECAY_TIMER

	Select as\asType
		Case ASSET_TEXTURE
			as\intVal = LoadTexture(as\file, flag)
		Case ASSET_IMAGE
			as\intVal = LoadImage(as\file)
		Case ASSET_MESH
			as\intVal = LoadMesh(as\file)
		Case ASSET_ANIM_MESH
			as\intVal = LoadAnimMesh(as\file)
	End Select

	DebugLog("CREATED ASSET: " + filePath)
	Return as
End Function

Function FreeAsset(as.AssetWrap)
	Select as\asType
		Case ASSET_TEXTURE
			FreeTexture(as\intVal)
		Case ASSET_IMAGE
			FreeImage(as\intVal)
		Case ASSET_MESH, ASSET_ANIM_MESH
			FreeEntity(as\intVal)
	End Select

	Local strng$ = as\file
	Delete as
	DebugLog("ASSET Removed: " + strng)
End Function

Function GrabAsset%(filePath$, asType%, flag%=1)
	Local as.AssetWrap
	For as = Each AssetWrap
		If (filePath = as\file) Then
			as\decayTimer = ASSET_DECAY_TIMER
			as\grabCount = as\grabCount + 1
			DebugLog("GRABBED ASSET: " + filePath + ", " + as\grabCount)
			Return as\intVal
		EndIf
	Next

	;Asset doesn't exist, create it.
	as = CreateAsset(filePath, asType, flag)
	as\grabCount = 1
	DebugLog("GRABBED ASSET: " + filePath + ", " + as\grabCount)

	Return as\intVal
End Function

Function GrabTexture%(filePath$, flag%=1)
	Return GrabAsset(filePath, ASSET_TEXTURE, flag)
End Function

Function GrabImage%(filePath$)
	Return GrabAsset(filePath, ASSET_IMAGE)
End Function

Function GrabMesh%(filePath$)
	Return GrabAsset(filePath, ASSET_MESH)
End Function

Function DropAsset(obj%)
	Local as.AssetWrap
	For as = Each AssetWrap
		If (obj = as\intVal) Then
			as\grabCount = as\grabCount - 1
			DebugLog("DROPPED ASSET: " + as\file + ", " + as\grabCount)
			Return
		EndIf
	Next

	;TODO: Maybe not make this crash the game later?
	RuntimeError("Attempted to drop non-existant asset.")
End Function

Function UpdateAssets()
	Local as.AssetWrap
	For as = Each AssetWrap
		If (as\grabCount < 1) Then
			as\decayTimer = as\decayTimer - timing\tickDuration
			DebugLog("ASSET DECAYING: " + as\file + ", " + as\decayTimer)
			If (as\decayTimer < 0) Then
				FreeAsset(as)
			EndIf
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
	MaskImage(uiAssets\tileBlack, 255,255,0)

	ResizeImage(uiAssets\back, ImageWidth(uiAssets\back) * MenuScale, ImageHeight(uiAssets\back) * MenuScale)
	ResizeImage(uiAssets\scpText, ImageWidth(uiAssets\scpText) * MenuScale, ImageHeight(uiAssets\scpText) * MenuScale)
	ResizeImage(uiAssets\scp173, ImageWidth(uiAssets\scp173) * MenuScale, ImageHeight(uiAssets\scp173) * MenuScale)

	uiAssets\pauseMenuBG = LoadImage("GFX/menu/pausemenu.jpg")
	MaskImage(uiAssets\pauseMenuBG, 255,255,0)
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

	uiAssets\sprintIcon = LoadImage("GFX/HUD/sprinticon.png")
	uiAssets\blinkIcon% = LoadImage("GFX/HUD/blinkicon.png")
	uiAssets\crouchIcon% = LoadImage("GFX/HUD/sneakicon.png")
	uiAssets\handIcon[HAND_ICON_TOUCH] = LoadImage("GFX/HUD/handsymbol.png")
	uiAssets\handIcon[HAND_ICON_GRAB] = LoadImage("GFX/HUD/handsymbol2.png")
	uiAssets\blinkBar = LoadImage("GFX/HUD/blinkmeter.jpg")
	uiAssets\staminaBar = LoadImage("GFX/HUD/staminameter.jpg")
	uiAssets\keypadHUD = LoadImage("GFX/HUD/keypadhud.jpg")
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
	TextureBlend(AmbientLightRoomTex,5)
	SetBuffer(TextureBuffer(AmbientLightRoomTex))
	ClsColor(0,0,0)
	Cls()
	SetBuffer(BackBuffer())
	AmbientLightRoomVal = 0

	SoundEmitter = CreatePivot()

	CreateBlurImage()
	CameraProjMode(ark_blur_cam,0)

	mainPlayer = CreatePlayer()

	AmbientLight(Brightness, Brightness, Brightness)

	ScreenTexs[0] = CreateTexture(512, 512, 1+256)
	ScreenTexs[1] = CreateTexture(512, 512, 1+256)

	;Listener = CreateListener(mainPlayer\cam)

	DrawLoading(5)
	TeslaTexture = LoadTexture("GFX/Map/Textures/tesla.jpg", 1+2)

	LightSpriteTex(0) = LoadTexture("GFX/Sprites/light1.jpg", 1)
	LightSpriteTex(1) = LoadTexture("GFX/Sprites/light2.jpg", 1)
	LightSpriteTex(2) = LoadTexture("GFX/Sprites/lightsprite.jpg",1)

	DrawLoading(10)

	DrawLoading(15)

	For i = 0 To 5
		GorePics(i) = LoadTexture("GFX/895pics/pic" + Str(i + 1) + ".jpg")
	Next

	OldAiPics(0) = LoadTexture("GFX/079pics/AIface.jpg")
	OldAiPics(1) = LoadTexture("GFX/079pics/AIface2.jpg")

	DrawLoading(20)

	;TODO: replace DecalTextures with a 2D array?
	For i = 0 To 6
		DecalTextures(i) = LoadTexture("GFX/Decals/decal" + Str(i + 1) + ".png", 1 + 2)
	Next
	;DecalTextures(7) = LoadTexture("GFX/items/INVpaperstrips.jpg", 1 + 2)
	For i = 8 To 12
		DecalTextures(i) = LoadTexture("GFX/Decals/decalpd"+(i-7)+".jpg", 1 + 2)
	Next
	For i = 13 To 14
		DecalTextures(i) = LoadTexture("GFX/Decals/bullethole"+Str(i-12)+".jpg", 1 + 2)
	Next
	For i = 15 To 16
		DecalTextures(i) = LoadTexture("GFX/Decals/blooddrop"+Str(i-14)+".png", 1 + 2)
	Next
	DecalTextures(17) = LoadTexture("GFX/Decals/decal8.png", 1 + 2)
	DecalTextures(18) = LoadTexture("GFX/Decals/decalpd6.jpg", 1 + 2)

	DrawLoading(25)

	Monitor = LoadMesh("GFX/Map/monitor.b3d")
	HideEntity(Monitor)
	MonitorTexture = LoadTexture("GFX/General/monitortexture.jpg")

	CamBaseOBJ = LoadMesh("GFX/Map/cambase.x")
	HideEntity(CamBaseOBJ)
	CamOBJ = LoadMesh("GFX/Map/CamHead.b3d")
	HideEntity(CamOBJ)

	InitItemTemplates()

	ParticleTextures(0) = LoadTexture("GFX/Sprites/smoke.png", 1 + 2)
	ParticleTextures(1) = LoadTexture("GFX/Sprites/flash.jpg", 1 + 2)
	ParticleTextures(2) = LoadTexture("GFX/Sprites/dust.jpg", 1 + 2)
	ParticleTextures(3) = LoadTexture("GFX/General/hg.jpg", 1 + 2)
	ParticleTextures(4) = LoadTexture("GFX/Map/Textures/sun.jpg", 1 + 2)
	ParticleTextures(5) = LoadTexture("GFX/Decals/bloodsprite.png", 1 + 2)
	ParticleTextures(6) = LoadTexture("GFX/Sprites/smoke2.png", 1 + 2)
	ParticleTextures(7) = LoadTexture("GFX/Sprites/spark.jpg", 1 + 2)

	LoadMaterials("Data/materials.ini")

	;TextureLodBias TextureFloat#

	DrawLoading(30)

	LoadRoomTemplates("Data/rooms.ini")

	;LoadRoomMeshes()

End Function

Function InitNewGame()
	Local i%, de.Decal, d.Door, it.Item, r.Room, sc.SecurityCam, e.Event, prop.Prop

	DrawLoading(45)

	LoadInGameSounds(sndManager)

	HideDistance# = 15.0

	mainPlayer\heartbeatIntensity = 70
	;HeartBeatRate = 70

	AccessCode = 0
	For i = 0 To 3
		AccessCode = AccessCode + Rand(1,9)*(10^i)
	Next

	If (SelectedMap = "") Then
		CreateMap()
	Else
		LoadMap("Map Creator/Maps/"+SelectedMap)
	EndIf
	InitWayPoints()

	DrawLoading(79)

	Curr173 = CreateNPC(NPCtype173, 0, -30.0, 0)
	Curr106 = CreateNPC(NPCtype106, 0, -30.0, 0)
	Curr106\state = 70 * 60 * Rand(12,17)

	For d.Door = Each Door
		EntityParent(d\obj, 0)
		If (d\obj2 > 0) Then EntityParent(d\obj2, 0)
		If (d\frameobj > 0) Then EntityParent(d\frameobj, 0)
		If (d\buttons[0] > 0) Then EntityParent(d\buttons[0], 0)
		If (d\buttons[1] > 0) Then EntityParent(d\buttons[1], 0)

		If (d\obj2 <> 0 And d\dir = 0) Then
			MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
			MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
		EndIf
	Next

	For it.Item = Each Item
		EntityType(it\collider, HIT_ITEM)
		EntityParent(it\collider, 0)
	Next

	DrawLoading(80)
	For sc.SecurityCam= Each SecurityCam
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next

	For r.Room = Each Room
		For i = 0 To MaxRoomLights
			If (r\lights[i]<>0) Then EntityParent(r\lights[i],0)
		Next

		If (Not r\roomTemplate\disableDecals) Then
			If (Rand(4) = 1) Then
				de.Decal = CreateDecal(Rand(2, 3), EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\size = Rnd(0.1, 0.4) : ScaleSprite(de\obj, de\size, de\size)
				EntityAlpha(de\obj, Rnd(0.85, 0.95))
			EndIf

			If (Rand(4) = 1) Then
				de.Decal = CreateDecal(0, EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\size = Rnd(0.5, 0.7) : EntityAlpha(de\obj, 0.7) : de\id = 1 : ScaleSprite(de\obj, de\size, de\size)
				EntityAlpha(de\obj, Rnd(0.7, 0.85))
			EndIf
		EndIf

		If (r\roomTemplate\name = "cont_173_1" And (userOptions\introEnabled = False)) Then
			PositionEntity(mainPlayer\collider, EntityX(r\obj)+3584*RoomScale, 714*RoomScale, EntityZ(r\obj)+1024*RoomScale)
			ResetEntity(mainPlayer\collider)
			mainPlayer\currRoom = r
		ElseIf (r\roomTemplate\name = "intro" And userOptions\introEnabled) Then
			PositionEntity(mainPlayer\collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
			ResetEntity(mainPlayer\collider)
			mainPlayer\currRoom = r
		EndIf

	Next

	Local rt.RoomTemplate
	For rt.RoomTemplate = Each RoomTemplate
		If (rt\collisionObjs<>Null) Then
			For i% = 0 To rt\collisionObjs\size-1
				FreeEntity(GetIntArrayListElem(rt\collisionObjs,i))
			Next
			DeleteIntArrayList(rt\collisionObjs) : rt\collisionObjs = Null
		EndIf

		FreeEntity(rt\opaqueMesh)
		If (rt\alphaMesh<>0) Then FreeEntity(rt\alphaMesh)

		If (rt\props<>Null) Then
			For i% = 0 To rt\props\size-1
				prop = Object.Prop(GetIntArrayListElem(rt\props,i))
				FreeEntity(prop\obj)
				Delete prop
			Next
			DeleteIntArrayList(rt\props) : rt\props = Null
		EndIf
	Next

	Local tw.TempWaypoint
	For tw.TempWaypoint = Each TempWaypoint
		Delete tw
	Next

	TurnEntity(mainPlayer\collider, 0, Rand(160, 200), 0)

	ResetEntity(mainPlayer\collider)

	If (SelectedMap = "") Then InitEvents()

	;TODO: fix
;	For e.Event = Each Event
;		If e\name = "room2nuke"
;			e\eventState = 1
;			DebugLog("room2nuke")
;		EndIf
;		If e\name = "room106"
;			e\eventState2 = 1
;			DebugLog("room106")
;		EndIf
;		If e\name = "room2sl"
;			e\eventState3 = 1
;			DebugLog("room2sl")
;		EndIf
;	Next

	MoveMouse(viewport_center_x,viewport_center_y);320, 240)

	SetFont(uiAssets\font[0])

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
		;Cls()
		If (Int(Float(i)*0.27)<>Int(Float(i-1)*0.27)) Then
			DrawLoading(80+Int(Float(i)*0.27))
		EndIf
	Next

	FreeTextureCache()
	DrawLoading(100)

	FlushKeys()
	FlushMouse()

	mainPlayer\dropSpeed = 0
End Function

Function InitLoadGame()
	Local d.Door, sc.SecurityCam, rt.RoomTemplate, e.Event, prop.Prop, planetex%, ch.Chunk

	DrawLoading(80)

	For d.Door = Each Door
		EntityParent(d\obj, 0)
		If (d\obj2 > 0) Then EntityParent(d\obj2, 0)
		If (d\frameobj > 0) Then EntityParent(d\frameobj, 0)
		If (d\buttons[0] > 0) Then EntityParent(d\buttons[0], 0)
		If (d\buttons[1] > 0) Then EntityParent(d\buttons[1], 0)

	Next

	For sc.SecurityCam = Each SecurityCam
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next

	ResetEntity(mainPlayer\collider)

	;InitEvents()

	DrawLoading(90)

	MoveMouse(viewport_center_x,viewport_center_y)

	SetFont(uiAssets\font[0])

	HidePointer()

	mainPlayer\blinkTimer = mainPlayer\blinkFreq
	mainPlayer\stamina = 100

	Local i%, x#, z#
	For rt.RoomTemplate = Each RoomTemplate
		If (rt\collisionObjs<>Null) Then
			For i% = 0 To rt\collisionObjs\size-1
				FreeEntity(GetIntArrayListElem(rt\collisionObjs,i))
			Next
			DeleteIntArrayList(rt\collisionObjs) : rt\collisionObjs = Null
		EndIf

		FreeEntity(rt\opaqueMesh)
		If (rt\alphaMesh<>0) Then FreeEntity(rt\alphaMesh)

		If (rt\props<>Null) Then
			For i% = 0 To rt\props\size-1
				prop = Object.Prop(GetIntArrayListElem(rt\props,i))
				FreeEntity(prop\obj)
				Delete prop
			Next
			DeleteIntArrayList(rt\props) : rt\props = Null
		EndIf
	Next

	mainPlayer\dropSpeed = 0.0

	;TODO: Not load this at the start of the game jesus.
	For e.Event = Each Event
		;Loading the necessary stuff for dimension1499, but this will only be done if the player is in this dimension already
		If (e\name = "dimension1499") Then
			If (e\eventState = 2) Then
				;[Block]
				DrawLoading(91)
				e\room\objects[0] = CreatePlane()
				planetex% = LoadTexture("GFX/Map/Rooms/dimension1499/grit3.jpg")
				EntityTexture(e\room\objects[0],planetex%)
				FreeTexture(planetex%)
				PositionEntity(e\room\objects[0],0,EntityY(e\room\obj),0)
				EntityType(e\room\objects[0],HIT_MAP)
				;EntityParent(e\room\objects[0],e\room\obj)
				DrawLoading(92)
				NTF_1499Sky = sky_CreateSky("GFX/Map/sky/1499sky")
				DrawLoading(93)
				For i = 1 To 15
					e\room\objects[i] = LoadMesh("GFX/Map/Rooms/dimension1499/1499object"+i+".b3d")
					HideEntity(e\room\objects[i])
				Next
				DrawLoading(96)
				CreateChunkParts(e\room)
				DrawLoading(97)
				x# = EntityX(e\room\obj)
				z# = EntityZ(e\room\obj)
				For i = -2 To 2 Step 2
					ch = CreateChunk(-1,x#*(i*2.5),EntityY(e\room\obj),z#)
				Next
				;If (Music(18)=0) Then Music(18) = LoadSound("SFX/Music/1499.ogg") ;TODO: fix
				DrawLoading(98)
				UpdateChunks(e\room,15,False)
				;MoveEntity(mainPlayer\collider,0,10,0)
				;ResetEntity(mainPlayer\collider)

				DebugLog("Loaded dimension1499 successful")

				Exit
				;[End Block]
			EndIf
		EndIf
	Next

	FreeTextureCache()

	DrawLoading(100)
End Function

;TODO: Go through this to fix memory leaks.
Function NullGame()
	Local i%, x%, y%, lvl%
	Local itt.ItemTemplate, s.Screen, lt.LightTemplate, d.Door, m.Material
	Local wp.Waypoint, twp.TempWaypoint, r.Room, it.Item

	DeloadInGameSounds(sndManager)

	ClearTextureCache()

	DeletePlayer(mainPlayer) : mainPlayer = Null

	DeathMSG$=""

	SelectedMap = ""

	DoorTempID = 0

	GameSaved = 0

	HideDistance# = 15.0

	Contained106 = False
	Curr173\idle = False

	MTFtimer = 0
	For i = 0 To 9
		MTFrooms[i]=Null
		MTFroomState[i]=0
	Next

	For s.Screen = Each Screen
		If (s\img <> 0) Then
			FreeImage(s\img)
			s\img = 0
		EndIf
		Delete s
	Next

	ConsoleInput = ""

	Msg = ""
	MsgTimer = 0

	Delete Each Door

	Delete Each LightTemplate

	Delete Each Material

	Delete Each WayPoint

	Delete Each TempWaypoint

	For r.Room = Each Room
		DeleteIntArray(r\collisionObjs)
		If (r\props<>Null) Then DeleteIntArray(r\props)
		Delete r
	Next
	DeleteIntArray(MapRooms)

	Delete Each RoomTemplate

	Local it.ItemTemplate
	For it = Each ItemTemplate
		Delete it
	Next

	Local i.Item
	For i = Each Item
		Delete i
	Next

	Delete Each Prop

	Delete Each Decal

	Local n.NPC
	For n.NPC = Each NPC
		RemoveNPC(n)
	Next
	Curr173 = Null
	Curr106 = Null
	Curr096 = Null
	For i = 0 To 6
		MTFrooms[i]=Null
	Next

	Local e.Event
	For e.Event = Each Event
		RemoveEvent(e)
	Next

	Delete Each SecurityCam

	Delete Each Emitter

	Delete Each Particle

	For i = 0 To 5
		If (IsChannelPlaying(RadioCHN(i))) Then StopChannel(RadioCHN(i))
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

	ClearWorld()
	ark_blur_cam = 0
	InitFastResize()

	For i=0 To 9
		If (TempSounds[i]<>0) Then
			FreeSound(TempSounds[i]) : TempSounds[i]=0
		EndIf
	Next

End Function

Function GetImagePath$(path$)
	If (FileType(path + ".png") = 1) Then
		Return path + ".png"
	EndIf
	Return path + ".jpg"
End Function










;~IDEal Editor Parameters:
;~C#Blitz3D