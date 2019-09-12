import os


def change_file_name(file_name, files):
    new_file_name = file_name
    file_name_without_extension, file_extension = os.path.splitext(file_name)
    if new_file_name in files:
        new_file_name = file_name_without_extension + " (1)" + file_extension
    i = 2
    while new_file_name in files:
        new_file_name = file_name_without_extension + " (" + str(i) + ")" + file_extension
        i += 1
    return new_file_name
