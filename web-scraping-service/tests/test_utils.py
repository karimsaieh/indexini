import unittest
import utils
import xmlrunner

#  python -m unittest
# coverage run -m unittest discover
# coverage html
#  coverage run -m xmlrunner discover -o junit

class UtilsTests(unittest.TestCase):
    def test_file_name_change(self):
        new_file_name  = utils.change_file_name( "file1.pdf", ["file1.pdf", "file2.pdf", "file1 (1).pdf"])
        self.assertEqual(new_file_name, "file1 (2).pdf")


if __name__ == "__main__":
    unittest.main(
        testRunner=xmlrunner.XMLTestRunner(output='test-reports'),
        # these make sure that some options that are not applicable
        # remain hidden from the help menu.
        failfast=False, buffer=False, catchbreak=False)
