package tests;
import model.HomePage;
import model.folder.FolderStatusPage;
import model.organization_folder.OrgFolderStatusPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectMethodsUtils;

import static runner.TestUtils.getRandomStr;

public class OrganizationFolderTest extends BaseTest {
    private static final String ORGANIZATION_FOLDER_NAME = getRandomStr();
    private static final String ORGANIZATION_FOLDER_RENAME = getRandomStr();
    private static final String FOLDER_NAME = getRandomStr();
    private static final String DISPLAY_NAME = getRandomStr();

    @Test
    public void testRenameOrganizationFolder() {
        ProjectMethodsUtils.createNewOrganizationFolder(getDriver(), ORGANIZATION_FOLDER_NAME);

        HomePage homePage = new HomePage(getDriver())
                .clickOrgFolder(ORGANIZATION_FOLDER_NAME)
                .getSideMenu()
                .clickRename()
                .clearFieldAndInputNewName(ORGANIZATION_FOLDER_RENAME)
                .clickRenameButton()
                .getBreadcrumbs()
                .clickDashboard();

        Assert.assertTrue(homePage.getJobNamesList().contains(ORGANIZATION_FOLDER_RENAME));
    }

    @Test
    public void testDeleteOrganizationFolderDependsMethods() {
        ProjectMethodsUtils.createNewOrganizationFolder(getDriver(), ORGANIZATION_FOLDER_NAME);

        HomePage homePage = new HomePage(getDriver())
                .clickOrgFolder(ORGANIZATION_FOLDER_NAME)
                .getSideMenu()
                .clickDeleteToHomePage()
                .clickYes();

        Assert.assertFalse(homePage.getJobNamesList().contains(ORGANIZATION_FOLDER_NAME));
    }

    @Test
    public void testConfigureOrganizationFolderWithName() {
        ProjectMethodsUtils.createNewOrganizationFolder(getDriver(), ORGANIZATION_FOLDER_NAME);

        OrgFolderStatusPage orgFolderStatusPage = new HomePage(getDriver())
                .clickOrgFolder(ORGANIZATION_FOLDER_NAME)
                .getSideMenu()
                .clickConfigure()
                .inputDisplayName(DISPLAY_NAME)
                .clickSaveButton();

        Assert.assertEquals(orgFolderStatusPage.getNameText(), DISPLAY_NAME);
    }

    @Test
    public void testConfigureOrganizationFolderWithDescription() {
        ProjectMethodsUtils.createNewOrganizationFolder(getDriver(), ORGANIZATION_FOLDER_NAME);
        final String description = getRandomStr();

        OrgFolderStatusPage orgFolderStatusPage = new HomePage(getDriver())
                .clickOrgFolder(ORGANIZATION_FOLDER_NAME)
                .getSideMenu()
                .clickConfigure()
                .inputDescription(description)
                .clickSaveButton();

        Assert.assertEquals(orgFolderStatusPage.getAdditionalDescriptionText(), description);
    }

    @Test
    public void testMoveOrgFolderToFolder() {
        ProjectMethodsUtils.createNewOrganizationFolder(getDriver(), ORGANIZATION_FOLDER_NAME);
        ProjectMethodsUtils.createNewFolder(getDriver(), FOLDER_NAME);

        FolderStatusPage folderStatusPage = new HomePage(getDriver())
                .clickOrgFolder(ORGANIZATION_FOLDER_NAME)
                .clickMoveButton()
                .selectFolder(FOLDER_NAME)
                .clickMove()
                .getBreadcrumbs()
                .clickDashboard()
                .clickFolder(FOLDER_NAME);

        Assert.assertTrue(folderStatusPage.getJobList().contains(ORGANIZATION_FOLDER_NAME));
    }

    @Test(dependsOnMethods = "testMoveOrgFolderToFolder")
    public void testMoveOrgFolderToDashboard() {
        HomePage homePage = new HomePage(getDriver())
                .clickFolder(FOLDER_NAME)
                .clickOrgFolder(ORGANIZATION_FOLDER_NAME)
                .clickMoveButton()
                .selectOptionToDashBoard()
                .clickMove()
                .getBreadcrumbs()
                .clickDashboard();

        Assert.assertTrue(homePage.getJobNamesList().contains(ORGANIZATION_FOLDER_NAME));
    }

    @Test
    public void testCheckChildHealthMetrics() {
        ProjectMethodsUtils.createNewOrganizationFolder(getDriver(), ORGANIZATION_FOLDER_NAME);

        boolean actualResult = new HomePage(getDriver())
                   .clickOrgFolder(ORGANIZATION_FOLDER_NAME)
                   .clickLinkConfigureTheProject()
                   .clickHealthMetricsTab()
                   .clickMetricsButton()
                   .checkChildMetricsIsDisplayed();

        Assert.assertTrue(actualResult);
    }
}