cmake_minimum_required(VERSION 3.10)

# set the project name
project(scpcb)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)
set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED True)

set(ASSIMP_BUILD_TESTS OFF)
set(ASSIMP_BUILD_ASSIMP_TOOLS OFF)

set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR})
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR})

add_compile_definitions(AS_NO_COMPILER)
# add_compile_definitions(_DX11)
add_compile_definitions(PGE_DEBUG)
# add_compile_definitions(LINUX)
add_compile_definitions(STB_IMAGE_IMPLEMENTATION STBI_WINDOWS_UTF8 STBI_FAILURE_USERMSG)

add_subdirectory(Engine)
add_subdirectory(Libraries/tinyxml2)
add_subdirectory(Libraries/assimp-4.1.0)
add_subdirectory(Libraries/freetype)
add_subdirectory(Libraries/angelscript/sdk/angelscript/projects/cmake)

# add the executable
file(GLOB_RECURSE SOURCE_FILES
    "${CMAKE_CURRENT_SOURCE_DIR}/Src/*.cpp"
    "${CMAKE_CURRENT_SOURCE_DIR}/Libraries/angelscript/sdk/add_on/scriptarray/*.cpp"
    )
add_executable(scpcb "${CMAKE_CURRENT_SOURCE_DIR}/Src/Main.cpp" "${SOURCE_FILES}")

# add_compile_definitions(OPEN_VR)

target_link_libraries(scpcb PUBLIC Engine)
target_link_libraries(scpcb PUBLIC tinyxml2)
target_link_libraries(scpcb PUBLIC assimp)
target_link_libraries(scpcb PUBLIC freetype)
target_link_libraries(scpcb PUBLIC angelscript)
target_link_libraries(scpcb PUBLIC SDL2)

target_include_directories(scpcb
                        PUBLIC
                        "${CMAKE_CURRENT_SOURCE_DIR}/Src"
                        "${CMAKE_CURRENT_SOURCE_DIR}/Engine/Include"
                        "${CMAKE_CURRENT_SOURCE_DIR}/Libraries/tinyxml2"
                        "${CMAKE_CURRENT_SOURCE_DIR}/Libraries/assimp-4.1.0/include"
                        "${CMAKE_CURRENT_SOURCE_DIR}/Libraries/freetype/include"
                        "${CMAKE_CURRENT_SOURCE_DIR}/Libraries/stb_image"
                        "${CMAKE_CURRENT_SOURCE_DIR}/Libraries/angelscript/sdk/angelscript/include"
                        "${CMAKE_CURRENT_SOURCE_DIR}/Libraries/angelscript/sdk/add_on"
                        )

if(MSVC)
    # set_target_properties(scpcb PROPERTIES WIN32_EXECUTABLE TRUE)
endif()


add_custom_command(TARGET scpcb PRE_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_directory
    ${CMAKE_SOURCE_DIR}/Content/ $<TARGET_FILE_DIR:scpcb>
    )

add_custom_command(TARGET scpcb PRE_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy
    ${CMAKE_SOURCE_DIR}/Windows/options.ini $<TARGET_FILE_DIR:scpcb>/options.ini
    )

# include(BuildShaders/CMakeLists.txt)