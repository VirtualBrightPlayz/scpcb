;TODO: remove once we move to C++, we'll be able to resize things with the graphics API
;in a more straightforward and efficient manner

Global fresize_image%, fresize_texture%, fresize_texture2%
Global fresize_cam%

Function ResizeImage2(image%,width%,height%)
    img% = CreateImage(width,height)
	
	oldWidth% = ImageWidth(image)
	oldHeight% = ImageHeight(image)
	CopyRect 0,0,oldWidth,oldHeight,1024-oldWidth/2,1024-oldHeight/2,ImageBuffer(image),TextureBuffer(fresize_texture)
	SetBuffer BackBuffer()
	ScaleRender(0,0,2048.0 / Float(GraphicWidth) * Float(width) / Float(oldWidth), 2048.0 / Float(GraphicWidth) * Float(height) / Float(oldHeight))
	;might want to replace Float(userOptions\screenWidth) with Max(userOptions\screenWidth,userOptions\screenHeight) if portrait sizes cause issues
	;everyone uses landscape so it's probably a non-issue
	CopyRect GraphicWidth/2-width/2,GraphicHeight/2-height/2,width,height,0,0,BackBuffer(),ImageBuffer(img)
	
    FreeImage image
    Return img
End Function

Function ScaleRender(x#,y#,hscale#=1.0,vscale#=1.0)
	If mainPlayer<>Null Then HideEntity mainPlayer\cam
	WireFrame 0
	ShowEntity fresize_image
	ScaleEntity fresize_image,hscale,vscale,1.0
	PositionEntity fresize_image, x, y, 1.0001
	ShowEntity fresize_cam
	RenderWorld()
	HideEntity fresize_cam
	HideEntity fresize_image
	WireFrame WireframeState
	If mainPlayer<>Null Then ShowEntity mainPlayer\cam
End Function

Function InitFastResize()
    ;Create Camera
	Local cam% = CreateCamera()
	CameraProjMode cam, 2
	CameraZoom cam, 0.1
	CameraClsMode cam, 0, 0
	CameraRange cam, 0.1, 1.5
	MoveEntity cam, 0, 0, -10000
	
	fresize_cam = cam
	
    ;ark_sw = GraphicsWidth()
    ;ark_sh = GraphicsHeight()
	
    ;Create sprite
	Local spr% = CreateMesh(cam)
	Local sf% = CreateSurface(spr)
	AddVertex sf, -1, 1, 0, 0, 0
	AddVertex sf, 1, 1, 0, 1, 0
	AddVertex sf, -1, -1, 0, 0, 1
	AddVertex sf, 1, -1, 0, 1, 1
	AddTriangle sf, 0, 1, 2
	AddTriangle sf, 3, 2, 1
	EntityFX spr, 17
	ScaleEntity spr, 2048.0 / Float(GraphicWidth), 2048.0 / Float(GraphicHeight), 1
	PositionEntity spr, 0, 0, 1.0001
	EntityOrder spr, -100001
	EntityBlend spr, 1
	fresize_image = spr
	
    ;Create texture
	fresize_texture = CreateTexture(2048, 2048, 1+256)
	fresize_texture2 = CreateTexture(2048, 2048, 1+256)
	TextureBlend fresize_texture2,3
	SetBuffer(TextureBuffer(fresize_texture2))
	ClsColor 0,0,0
	Cls
	SetBuffer(BackBuffer())
	;TextureAnisotropy(fresize_texture)
	EntityTexture spr, fresize_texture,0,0
	EntityTexture spr, fresize_texture2,0,1
	
	HideEntity fresize_cam
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D