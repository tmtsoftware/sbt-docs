trait Api {

  /**
   * Reads data from file in string
   *
   * @param name of the file
   * @return data
   */
  def readFile(name: String): String
}
