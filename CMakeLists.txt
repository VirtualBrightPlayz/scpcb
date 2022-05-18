cmake_minimum_required(VERSION 3.21)
project(scpcb VERSION 0.1.0)

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${CMAKE_SOURCE_DIR}/cmake/)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BUILD_RPATH})

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)
set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED True)

set(ASSIMP_BUILD_TESTS OFF)
set(ASSIMP_BUILD_ASSIMP_TOOLS OFF)

option(D3D "DirectX11 (Windows Only)" ON)
option(VKR "Vulkan (Windows & Linux only)" ON)

#find_package(SDL2 REQUIRED FATAL_ERROR)
if(WIN32 AND D3D)
    add_compile_definitions(_D3D)
endif()
if(VKR AND NOT APPLE)
    find_package(Vulkan REQUIRED FATAL_ERROR)
    add_compile_definitions(_VKR)
endif()
add_compile_definitions(PGE_DEBUG)
if(UNIX AND NOT APPLE)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fno-strict-aliasing") # angelscript
    add_compile_definitions(LINUX)
endif()
add_compile_definitions(ANGELSCRIPT_EXPORT)

add_subdirectory(Engine)
add_subdirectory(Libraries/assimp)
add_subdirectory(Libraries/freetype)
# add_subdirectory(Libraries/openvr)
add_subdirectory(Libraries/angelscript/sdk/angelscript/projects/cmake)
# add_subdirectory(Libraries/tinyxml2) # this seems to break cmake for some reason
add_subdirectory(Src)
